package com.live.fluppertestapplication.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.live.fluppertestapplication.BuildConfig
import com.live.fluppertestapplication.R
import java.io.*
import java.util.*

class ImageUtils(){
    companion object{
         var imgPath: String = ""
         var imageUri: Uri? = null

        /****************************for open camera  and gallery intent*****************************************/
         fun getPickImageIntent(activity: Activity): Intent? {
            imageUri=null
            imgPath=""
            var chooserIntent: Intent? = null

            var intentList: MutableList<Intent> = ArrayList()

            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(activity))

            intentList = addIntentsToList(activity, intentList, pickIntent)
            intentList = addIntentsToList(activity, intentList, takePhotoIntent)

            if (intentList.size > 0) {
                chooserIntent = Intent.createChooser(
                        intentList.removeAt(intentList.size - 1),
                        activity.getString(R.string.select_capture_image)
                )
                chooserIntent!!.putExtra(
                        Intent.EXTRA_INITIAL_INTENTS,
                        intentList.toTypedArray<Parcelable>()
                )
            }

            return chooserIntent
        }

         fun setImageUri(activity: Activity): Uri {
            val folder = File("${activity.getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
            folder.mkdirs()

            val file = File(folder, "Image_Tmp.jpg")
            if (file.exists())
                file.delete()
            file.createNewFile()
            imageUri = FileProvider.getUriForFile(
                    activity,
                    BuildConfig.APPLICATION_ID + activity.getString(R.string.file_provider_name),
                    file
            )
            imgPath = file.absolutePath
            return imageUri!!
        }


        private fun addIntentsToList(
                context: Context,
                list: MutableList<Intent>,
                intent: Intent
        ): MutableList<Intent> {
            val resInfo = context.packageManager.queryIntentActivities(intent, 0)
            for (resolveInfo in resInfo) {
                val packageName = resolveInfo.activityInfo.packageName
                val targetedIntent = Intent(intent)
                targetedIntent.setPackage(packageName)
                list.add(targetedIntent)
            }
            return list
        }
        /*******************for stop rotating portrait image into landscape***************************************/
        fun imageOrientation(path: String?, context: Context): String? {
            var ei: ExifInterface? = null
            try {
                ei = ExifInterface(path!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val orientation = ei!!.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED)
            val bitmap = BitmapFactory.decodeFile(path)
            var rotatedBitmap: Bitmap? = null
            rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_UNDEFINED -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }
            return getRealPathFromURI(rotatedBitmap, context)
        }

        fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
            val matrix = Matrix()
            matrix.postRotate(angle)
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height,
                    matrix, true)
        }

        /*********************convert uri to String***************************/
        fun getRealPathFromURI(bitmap: Bitmap?, context: Context): String? {
            val contentURI = bitmapToFile(bitmap, context)
            val result: String?
            val cursor = context.contentResolver.query(contentURI, null, null, null, null)
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.path
            } else {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
                cursor.close()
            }
            return result
        }

        /*********************convert bitmap to uri***************************/
        fun bitmapToFile(bitmap: Bitmap?, context: Context): Uri {
            val folder = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString())
            folder.mkdirs()
            val file = File(folder, System.currentTimeMillis().toString() + ".jpg")
            if (file.exists()) file.delete()
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            var stream: OutputStream? = null
            try {
                stream = FileOutputStream(file)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            try {
                stream!!.flush()
                stream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return Uri.parse(file.absolutePath)
        }

        /******************compress image*******************************/
        open fun resizeAndCompressImageBeforeSend(context: Context, filePath: String?): String? {
            val compressQuality = 90
            val imageResolution = 900
            var bmpPic = BitmapFactory.decodeFile(filePath)
            val file = File(filePath)
            val filename = file.name
            val bos = ByteArrayOutputStream()
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bos)
            if (bmpPic.height >= imageResolution && bmpPic.width >= imageResolution) {
                bmpPic = getResizedBitmap(bmpPic, imageResolution)
            }
            try {
                val bmpFile = FileOutputStream(context.cacheDir.toString() + filename)
                bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile)
                bmpFile.flush()
                bmpFile.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return context.cacheDir.toString() + filename
        }

        fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
            var width = image.width
            var height = image.height
            val bitmapRatio = width.toFloat() / height.toFloat()
            if (bitmapRatio > 1) {
                width = maxSize
                height = (width / bitmapRatio).toInt()
            } else {
                height = maxSize
                width = (height * bitmapRatio).toInt()
            }
            return Bitmap.createScaledBitmap(image, width, height, true)
        }

    }
}