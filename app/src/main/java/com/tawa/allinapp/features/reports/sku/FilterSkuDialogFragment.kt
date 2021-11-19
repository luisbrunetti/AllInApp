package com.tawa.allinapp.features.reports.sku

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.tawa.allinapp.R
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.DialogFilterSkuBinding


class FilterSkuDialogFragment(
    val baseFragment: BaseFragment
): DialogFragment() {

    private lateinit var binding: DialogFilterSkuBinding
    var params = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.FILL_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    private val listCategory = ArrayList<CheckBox>()
    private var listCategoryFilter = ArrayList<String>()
    var listener: Callback? = null
    var lat = ""
    var long = ""
    var nameGen= ""
    companion object {

        fun newInstance(baseFragment: BaseFragment, dataExt : ArrayList<String>): FilterSkuDialogFragment {
            val frag = FilterSkuDialogFragment(baseFragment = baseFragment)
            val bundle = Bundle()
            bundle.putStringArrayList("data", ArrayList(HashSet(dataExt).sorted()))
            frag.arguments = bundle
            return frag
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogFilterSkuBinding.inflate(inflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            bundle.getStringArrayList("data")?.let {
                listCategoryFilter= it
                addCategories(it,binding.lnContent)
            }
        }
        binding.btnCloseModalFilter.setOnClickListener {
            dismiss()
        }
        binding.btnCleanFilters.setOnClickListener {
            addCategories(listCategoryFilter,binding.lnContent)
        }
        binding.btnFilterSku.setOnClickListener {
            val listSelected = ArrayList<String>()
            for(filter in listCategory)
            {
                if(filter.isChecked)
                {
                    listSelected.add(filter.text.toString())
                }
            }
            dismiss()
             listener?.onFilter(listSelected)
        }
        changeViewFragment()
    }

    private fun changeViewFragment(){
        baseFragment.translateObject.apply{
            binding.textView24.text = findTranslate("tvFilterCategoryFilterDialog") ?: "Filtrar por categoría:"
            binding.btnFilterSku.text = findTranslate("btnFilterFilterDialog") ?: "Filtrar"
            binding.btnCleanFilters.text = findTranslate("btnClearFilterDialog") ?: "Limpiar"
        }
    }

    private fun addCategories(list:ArrayList<String>,linearContent:LinearLayout){
        linearContent.removeAllViews()
        listCategory.removeAll(listCategory)
        for(category in 0 until list.size)
        {
            val checkBox = CheckBox(context)
            listCategory.add(checkBox)
            listCategory[category].text = list[category]
            listCategory[category].setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextAll))
            params.bottomMargin = 11f.toDips().toInt()
            listCategory[category].layoutParams = params
            linearContent.addView(listCategory[category])
        }
    }
    private fun Float.toDips() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    interface Callback {
        fun onFilter(list:ArrayList<String>)
    }
}