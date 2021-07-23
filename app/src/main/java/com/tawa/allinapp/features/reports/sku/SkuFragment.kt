package com.tawa.allinapp.features.reports.sku

import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.core.view.size
import com.tawa.allinapp.R
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentSkuBinding
import com.tawa.allinapp.models.Sku


class SkuFragment : BaseFragment() {

    private lateinit var binding: FragmentSkuBinding
    private lateinit var listSku:List<Sku>
    private lateinit var listLimitedSku:List<Sku>
    var listFilter:MutableList<Sku> = arrayListOf()
    private var product= ArrayList<String>()
    var date = ArrayList<String>()
    private var category = ArrayList<String>()
    private var idSku = ArrayList<String>()
    private var categoryFilter = ArrayList<String>()
    var subCategory = ArrayList<String>()
    var inventory = ArrayList<String>()
    var price = ArrayList<String>()
    private val numPages  = ArrayList<Int>()
    var listAnt= ArrayList<String>()
    private val btnObs = ArrayList<Button>()
    private var pageNum = 0

    val mapObservations = mutableMapOf<String,ArrayList<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSkuBinding.inflate(inflater)
        listSku = listOf(
                               Sku("1","7UP 1500ml","22/06/2021","Bebida saborizada 1","7UP","35","1.00"),
                               Sku("2","7UP 1500ml","22/06/2021","Bebida saborizada 2","7UP","35","1.00"),
                               Sku("3","7UP 1500ml","22/06/2021","Bebida saborizada 3","7UP","35","1.00"),
                               Sku("4","7UP 1500ml","22/06/2021","Bebida saborizada 4","7UP","35","1.00"),
                               Sku("5","7UP 1500ml","22/06/2021","Bebida saborizada 5","7UP","35","1.00"),
                               Sku("6","7UP 1500ml","22/06/2021","Bebida saborizada 6","7UP","35","1.00")
        )

        showTable()
        binding.btnFilterCat.setOnClickListener {
            showFilterDialog(categoryFilter)
        }
        return binding.root
    }

    private  fun showTable(){
        removeArray()
        binding.tlSku.removeViews(1,binding.tlSku.size-1)
        binding.tlDataSku.removeViews(1,binding.tlDataSku.size-1)
        numPages.removeAll(numPages)
        pageNum = 0
        listLimitedSku  = listSku.slice(0..4)
        for(cate in listSku)
            categoryFilter.add(cate.category!!)

        addElements(listLimitedSku)
        setSkuProduct(binding.tlSku,product)
        setSkuData(binding.tlDataSku,date,category,subCategory,inventory,price,idSku)
        createPager(listSku,binding.tlSku,binding.tlDataSku)
    }

    private  fun showTableFilter(list:ArrayList<String>){
        listFilter.removeAll(listFilter)
        for(i in list )
            listFilter.addAll(listSku.filter { it.category == i })
        removeArray()
        binding.tlSku.removeViews(1,binding.tlSku.size-1)
        binding.tlDataSku.removeViews(1,binding.tlDataSku.size-1)
        numPages.removeAll(numPages)
        pageNum = 0
        if(listFilter.size<5)
            listLimitedSku = listFilter.slice(0 until listFilter.size)
        else
            listLimitedSku = listFilter.slice(0..4)

        addElements(listLimitedSku)
        setSkuProduct(binding.tlSku,product)
        setSkuData(binding.tlDataSku,date,category,subCategory,inventory,price,idSku)
        createPager(listFilter,binding.tlSku,binding.tlDataSku)
    }


    private fun setSkuProduct(tableLayout: TableLayout, data:ArrayList<String>){
        for(name in data)
        {
            val row  = TableRow(context)
            row.setBackgroundResource(R.drawable.borderbottom)
            val textView = TextView(context)
            textView.text = name
            textView.textSize =12f
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextAll))
            textView.setTypeface(textView.typeface, Typeface.BOLD)
            textView.height=60f.toDips().toInt()
            textView.width = 150f.toDips().toInt()
            textView.setPadding(5)
            textView.gravity = Gravity.CENTER
            row.addView(textView)
            tableLayout.addView(row)
        }
    }

    private fun setSkuData(tableLayout: TableLayout, date:ArrayList<String>, category : ArrayList<String>,subcategory:ArrayList<String>,inventory:ArrayList<String>,price:ArrayList<String>,idSku:ArrayList<String>){

        for((flag, dateUnit) in date.withIndex())
        {
            val row  = TableRow(context)
            row.setBackgroundResource(R.drawable.borderbottomtlcontent)

            val  tvDate = TextView(context)
            textViewStyle(tvDate,dateUnit)
            row.addView(tvDate)

            val  tvCategory = TextView(context)
            textViewStyle(tvCategory,category[flag])
            row.addView(tvCategory)

            val  tvSubCategory = TextView(context)
            textViewStyle(tvSubCategory,subcategory[flag])
            row.addView(tvSubCategory)

            val  tvInventory = TextView(context)
            textViewStyle(tvInventory,inventory[flag])
            row.addView(tvInventory)

            val  tvPrice = TextView(context)
            textViewStyle(tvPrice,price[flag])
            row.addView(tvPrice)

            val checkStock = CheckBox(context)
            row.addView(checkStock)

            val checkExhibition = CheckBox(context)
            row.addView(checkExhibition)

            val editTextPrice = EditText(context)
            editTextPrice.hint = "0.00"
            editTextPrice.inputType = InputType.TYPE_CLASS_NUMBER
            editTextPrice.setBackgroundResource(R.drawable.rounded)
            editTextPrice.width = 20f.toDips().toInt()
            editTextPrice.setPadding(10f.toDips().toInt())
            row.addView(editTextPrice)

            val buttonObservations = Button(context)
            btnObs.add(buttonObservations)
            btnObs[flag].text = "+ Agregar"
            btnObs[flag].isAllCaps = false
            btnObs[flag].textSize = 18f
            val params = TableRow.LayoutParams(
                144f.toDips().toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.marginStart = 35f.toDips().toInt()
            params.marginEnd = 12f.toDips().toInt()
            btnObs[flag].layoutParams = params
            btnObs[flag].setBackgroundResource(R.drawable.bg_blue_button_sku)
            btnObs[flag].setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            btnObs[flag].tag = idSku[flag]
            btnObs[flag].setOnClickListener {
                showObservationDialog(idSku[flag])
            }
            row.addView(btnObs[flag])

            tableLayout.addView(row)
        }
    }

    private fun limitedTable(tl1:TableLayout,tl2:TableLayout,item:Int){
        listLimitedSku = listSku.slice((item*5)..(item*5+(numPages[item]-1)))
        removeArray()
        tl1.removeViews(1,tl1.size-1)
        tl2.removeViews(1,tl2.size-1)
        btnObs.removeAll(btnObs)
        addElements(listLimitedSku)
        setSkuProduct(binding.tlSku,product)
        setSkuData(binding.tlDataSku,date,category,subCategory,inventory,price,idSku)

    }

    private fun createPager(list:List<Sku>, tl1:TableLayout, tl2:TableLayout){
        val numPager: Int
        if(list.size%5==0) {
            numPager=  list.size/5
            for(i in 0 until (list.size/5) )
            {
                numPages.add(listLimitedSku.size)
            }
        }
        else {
            numPager = (list.size/5)+1
            for(i in 0 until (list.size/5) )
            {
                numPages.add(listLimitedSku.size)
            }
            numPages.add(list.size%5)
        }
        "${pageNum*5+1} - ${pageNum*5+numPages[pageNum]} de ${list.size} entradas".also { binding.tvPager.text = it }
        binding.btnPrevPage.setOnClickListener{
            if(pageNum>0)
            {
                pageNum--
                "${pageNum*5+1} - ${pageNum*5+numPages[pageNum]} de ${list.size} entradas".also { binding.tvPager.text = it }
                limitedTable(tl1,tl2,pageNum)
            }
        }
        binding.btnNextPage.setOnClickListener{
            if(pageNum<(numPager-1))
            {
                pageNum++
                "${pageNum*5+1} - ${pageNum*5+numPages[pageNum]} de ${list.size} entradas".also { binding.tvPager.text = it }
                limitedTable(tl1,tl2,pageNum)
            }
        }

    }
    private fun addElements(list:List<Sku>){
        list.forEach { category.add(it.category!!)
                            date.add(it.date!!)
                            product.add(it.product!!)
                            subCategory.add(it.subCategory!!)
                            inventory.add(it.inventory!!)
                            price.add(it.price!!)
                            idSku.add(it.id!!)}
    }

    private fun removeArray(){
        product.removeAll(product)
        date.removeAll(date)
        category.removeAll(category)
        subCategory.removeAll(subCategory)
        inventory.removeAll(inventory)
        price.removeAll(price)
        idSku.removeAll(idSku)
    }

    private fun textViewStyle(textView: TextView, text:String){
        textView.text =text
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextAll))
        textView.height=60f.toDips().toInt()
        textView.setPadding(15f.toDips().toInt())
        textView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
    }

    private fun showFilterDialog(data:ArrayList<String>){
        val dialog = FilterSkuDialogFragment.newInstance(data)
        dialog.show(childFragmentManager, "dialog")
        dialog.listener = object : FilterSkuDialogFragment.Callback{
            override fun onFilter(list:ArrayList<String>) {
                if(list.size==0)
                    showTable()
                else
                    showTableFilter(list)
            }
        }
    }

    private fun showObservationDialog(id:String){
        val dialog = ObservationsDialogFragment.newInstance(id,mapObservations)
        dialog.show(childFragmentManager, "dialog")
        dialog.listener = object : ObservationsDialogFragment.Callback{
            override fun onClick(id:String,list:String) {
                mapObservations.getOrPut(id) { arrayListOf() }.add(list)
                for(btn in btnObs)
                {
                    if(btn.tag == id)
                    {
                        when(val count = mapObservations[id]?.size)
                        {
                            1-> "$count Observación".also { btn.text = it }
                            else -> "$count Observaciones".also { btn.text = it }
                        }
                    }

                }
            }
        }
    }

    private fun Float.toDips() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}