package com.live.fluppertestapplication.colorPickerDialog.colorpickerAdapter

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.live.fluppertestapplication.colorPickerDialog.colorUtil.ColorUtil
import com.live.fluppertestapplication.colorPickerDialog.model.ColorShape
import kotlinx.android.synthetic.main.adapter_material_color_picker.view.*

class MaterialColorPickerAdapter(private val colors: List<String>,val isDialogOpen:Boolean) :
    RecyclerView.Adapter<MaterialColorPickerAdapter.MaterialColorViewHolder>() {

    private var isDarkColor = false
    private var color = ""
    private var selectedColorsList:ArrayList<String> = ArrayList<String>()
    private var colorShape = ColorShape.CIRCLE

    init {
        val darkColors = colors.count { ColorUtil.isDarkColor(it) }
        isDarkColor = (darkColors * 2) >= colors.size
    }

    fun setColorShape(colorShape: ColorShape) {
        this.colorShape = colorShape
    }

    fun setDefaultColor(color: String) {
        this.color = color
    }

  //  fun getSelectedColor() = color
    fun getSelectedColor() = selectedColorsList

    fun getItem(position: Int) = colors[position]

    override fun getItemCount() = colors.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialColorViewHolder {
        val rootView = ColorViewBinding.inflateAdapterItemView(parent)
        return MaterialColorViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: MaterialColorViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MaterialColorViewHolder(private val rootView: View) :
        RecyclerView.ViewHolder(rootView) {

        private val colorView = rootView.colorView
        private val checkIcon = rootView.checkIcon

        init {
            rootView.setOnClickListener {
                if (isDialogOpen) {
                    if (checkIcon.visibility == View.VISIBLE) {
                        checkIcon.visibility = View.GONE
                        if (selectedColorsList.contains(colors[it.tag as Int])) {
                            selectedColorsList.remove(colors[it.tag as Int])
                        }
                    } else {
                        checkIcon.visibility = View.VISIBLE
                        checkIcon.setColorFilter(if (isDarkColor) Color.WHITE else Color.BLACK)
                        selectedColorsList.add(colors[it.tag as Int])

                    }
                }
            }
        }

        fun bind(position: Int) {
            val color = getItem(position)
            rootView.tag = position
            ColorViewBinding.setBackgroundColor(colorView, color)
            ColorViewBinding.setCardRadius(colorView, colorShape)
            checkIcon.setColorFilter(if (isDarkColor) Color.WHITE else Color.BLACK)
        }

    }
}
