package com.tawa.allinapp.features.reports.sku

import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.Log
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
import com.tawa.allinapp.core.extensions.observe
import com.tawa.allinapp.core.extensions.viewModel
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentSkuBinding
import com.tawa.allinapp.models.Lines
import com.tawa.allinapp.models.SkuDetail
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList


class SkuFragment : BaseFragment() {

    private lateinit var binding: FragmentSkuBinding
    private lateinit var skuViewModel: SkuViewModel
    private lateinit var listSku:List<SkuDetail>
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var listLimitedSku:List<SkuDetail>
    var listFilter:MutableList<SkuDetail> = arrayListOf()
    private var product= ArrayList<String>()
    var date = ArrayList<String>()
    private var category = ArrayList<String>()
    private var idSkuDetail = ArrayList<String>()
    private var categoryFilter = ArrayList<String>()
    var subCategory = ArrayList<String>()
    var inventory = ArrayList<Int>()
    var price = ArrayList<Float>()
    var stockSaved = ArrayList<Boolean>()
    var exhiSave= ArrayList<Boolean>()
    var newPrice = ArrayList<Float>()
    private val numPages  = ArrayList<Int>()
    var listAnt= ArrayList<String>()
    private val btnObs = ArrayList<Button>()
    private val checksStock = ArrayList<CheckBox>()
    private val checkEx = ArrayList<CheckBox>()
    private val edPrice = ArrayList<EditText>()
    private var pageNum = 0
    private val mapCheckStock = mutableMapOf<String,Boolean?>()
    private val mapCheckEx = mutableMapOf<String,Boolean?>()
    private val mapEdPrice = mutableMapOf<String,Float?>()
    private val mapObs = mutableMapOf<String,ArrayList<String>>()
    var idReportPv:String = ""
    var idPv:String= ""
    var idCompany: String = ""
    var idSkuUpdate  = ""
    var skuType = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSkuBinding.inflate(inflater)
        skuViewModel = viewModel(viewModelFactory) {
            observe(successSku, { it?.let {
               if(typeSku.value==1)
               {
                    listSku = it
                    for(sku in it)
                    {
                        mapCheckStock[sku.id]=null
                        mapCheckEx[sku.id]=null
                        mapEdPrice[sku.id]=null
                    }
                    showTable()
               }
                if(typeSku.value==2)
                {
                    for(det in it)
                    {
                        skuViewModel.getSkuObservation(det.id,2)
                    }
                }
            } })
            observe(successGetSkuObservation, { it?.let {
                if(typeSku.value==1)
                {
                    if(it.isNotEmpty())
                    {
                        for(btn in btnObs)
                        {
                            if (btn.tag==it[0].idSkuDetail)
                            {
                                when(it.size)
                                {
                                    1-> (it.size.toString()+" Observacion").also { btn.text = it }
                                    else -> (it.size.toString()+" Observaciones").also { btn.text = it }
                                }
                            }
                        }
                    }
                }
                if(typeSku.value==2)
                {
                    for (data in it)
                    {
                        mapObs.getOrPut(data.idSkuDetail){ arrayListOf()}.add(data.observation)
                    }
                }

            } })
            observe(successGetSku, { it?.let {
                getLastLocation()
                if(typeIni.value==1)
                {
                    for(sku in it)
                    {
                        idSkuUpdate = sku.id
                        idReportPv = sku.id
                        idPv = sku.idPv
                        idCompany = sku.idCompany
                        skuViewModel.getSkuDetail(sku.id,1)
                    }
                }
                if(typeIni.value==2)
                {
                    for(sku in it)
                        skuViewModel.getSkuDetail(sku.id,2)
                }

            } })
            observe(successGetTypeSku,{it?.let{
                if(it.isNotEmpty()){
                    if(typeUpdate.value==1)
                        skuType = it
                    else{
                        if(it=="Terminado") {
                            showConfirmSyncDialog()
                        }
                       // activity?.onBackPressed()
                    }
                }

            }})
            observe(successUpdateStateSku,{it?.let {
                if(it)
                    skuViewModel.getTypeSku(2)
            }})
            observe(successSyncSku,{it?.let {
                if(it)
                    activity?.onBackPressed()
            }})
        }
        skuViewModel.getSku(1)
        binding.btnFilterCat.setOnClickListener {
            showFilterDialog(categoryFilter)
        }
        binding.btnSaveSku.setOnClickListener {
            if(skuType!="Terminado"){
                mapObs.clear()
                for(check in checksStock)
                    mapCheckStock[check.tag.toString()] = check.isChecked
                for(checkEx in checkEx)
                    mapCheckEx[checkEx.tag.toString()]= checkEx.isChecked
                for(ed in edPrice) {
                    if (ed.text.toString().isNotEmpty()) {
                        mapEdPrice[ed.tag.toString()] = ed.text.toString().toFloat()
                    }
                }
                skuViewModel.getSku(2)
                getLastLocation()
                showConfirmDialog("Terminado")
            }
            else
                notify(activity, R.string.register_ready)
        }
        binding.btnBrSku.setOnClickListener {
            if(skuType!="Terminado"){
                mapObs.clear()
                for(check in checksStock)
                    mapCheckStock[check.tag.toString()] = check.isChecked
                for(checkEx in checkEx)
                    mapCheckEx[checkEx.tag.toString()]= checkEx.isChecked
                for(ed in edPrice) {
                    if (ed.text.toString().isNotEmpty()) {
                        mapEdPrice[ed.tag.toString()] = ed.text.toString().toFloat()
                    }
                }
                skuViewModel.getSku(2)
                showConfirmDialog("Borrador")
            }
            else
                notify(activity, R.string.register_ready)
        }
        skuViewModel.getTypeSku(1)
        changeViewsFragment()
        return binding.root
    }


    override fun changeViewsFragment() {

    }
    private fun showTable(){
        removeArray()
        binding.tlSku.removeViews(1,binding.tlSku.size-1)
        binding.tlDataSku.removeViews(1,binding.tlDataSku.size-1)
        numPages.removeAll(numPages)
        btnObs.removeAll(btnObs)
        checksStock.removeAll(checksStock)
        checkEx.removeAll(checkEx)
        edPrice.removeAll(edPrice)
        pageNum = 0
        listLimitedSku  = listSku.slice(0..4)
        for(cate in listSku)
            categoryFilter.add(cate.category!!)

        addElements(listLimitedSku)
        setSkuProduct(binding.tlSku,product)
        setSkuData(binding.tlDataSku,date,category,subCategory,inventory,price,idSkuDetail)
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
        btnObs.removeAll(btnObs)
        checksStock.removeAll(checksStock)
        checkEx.removeAll(checkEx)
        edPrice.removeAll(edPrice)

        pageNum = 0
        if(listFilter.size<5)
            listLimitedSku = listFilter.slice(0 until listFilter.size)
        else
            listLimitedSku = listFilter.slice(0..4)

        addElements(listLimitedSku)
        setSkuProduct(binding.tlSku,product)
        setSkuData(binding.tlDataSku,date,category,subCategory,inventory,price,idSkuDetail)
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
            textView.height = 60f.toDips().toInt()
            textView.width  = 150f.toDips().toInt()
            textView.setPadding(5)
            textView.gravity = Gravity.CENTER
            row.addView(textView)
            tableLayout.addView(row)
        }
    }

    private fun setSkuData(tableLayout: TableLayout, date:ArrayList<String>, category : ArrayList<String>,subcategory:ArrayList<String>,inventory:ArrayList<Int>,price:ArrayList<Float>,idSku:ArrayList<String>){

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
            textViewStyle(tvInventory,inventory[flag].toString())
            row.addView(tvInventory)

            val  tvPrice = TextView(context)
            textViewStyle(tvPrice,price[flag].toString())
            row.addView(tvPrice)

            val checkStock = CheckBox(context)
            checksStock.add(checkStock)
            checksStock[flag].tag=idSku[flag]
            val type = mapCheckStock[idSku[flag]]
            if (type != null)
                checksStock[flag].isChecked=type
            else
                checksStock[flag].isChecked=stockSaved[flag]
            row.addView(checksStock[flag])

            val checkExhibition = CheckBox(context)
            checkEx.add(checkExhibition)
            checkEx[flag].tag=idSku[flag]
            val typeEx =mapCheckEx[idSku[flag]]
            if (typeEx != null)
                checkEx[flag].isChecked=typeEx
            else
                checkEx[flag].isChecked=exhiSave[flag]
            row.addView(checkEx[flag])

            val editTextPrice = EditText(context)
            edPrice.add(editTextPrice)
            edPrice[flag].hint = "0.00"
            edPrice[flag].inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
            edPrice[flag].setBackgroundResource(R.drawable.rounded)
            edPrice[flag].width = 20f.toDips().toInt()
            edPrice[flag].setPadding(10f.toDips().toInt())
            edPrice[flag].tag=idSku[flag]
            val textPrice = mapEdPrice[idSku[flag]]
            if(textPrice!=null)
                edPrice[flag].setText(textPrice.toString())
            else {
                if(newPrice[flag]!=0.0f)
                    edPrice[flag].setText(newPrice[flag].toString())
                else
                    edPrice[flag].setText("")
            }
            row.addView(edPrice[flag])

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
            skuViewModel.getSkuObservation(idSku[flag],1)
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
        checksStock.removeAll(checksStock)
        checkEx.removeAll(checkEx)
        edPrice.removeAll(edPrice)
        addElements(listLimitedSku)
        setSkuProduct(binding.tlSku,product)
        setSkuData(binding.tlDataSku,date,category,subCategory,inventory,price,idSkuDetail)

    }

    private fun createPager(list:List<SkuDetail>, tl1:TableLayout, tl2:TableLayout){
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
            for(check in checksStock)
                mapCheckStock[check.tag.toString()] = check.isChecked
            for(checkEx in checkEx)
                mapCheckEx[checkEx.tag.toString()]= checkEx.isChecked
            for(ed in edPrice)
                if(ed.text.toString().isNotEmpty())
                {
                    mapEdPrice[ed.tag.toString()]=ed.text.toString().toFloat()
                }


            if(pageNum>0)
            {
                pageNum--
                "${pageNum*5+1} - ${pageNum*5+numPages[pageNum]} de ${list.size} entradas".also { binding.tvPager.text = it }
                limitedTable(tl1,tl2,pageNum)
            }
        }
        binding.btnNextPage.setOnClickListener{

            for(check in checksStock)
                mapCheckStock[check.tag.toString()] = check.isChecked
            for(checkEx in checkEx)
                mapCheckEx[checkEx.tag.toString()]= checkEx.isChecked
            for(ed in edPrice)
                if(ed.text.toString().isNotEmpty())
                {
                    mapEdPrice[ed.tag.toString()]=ed.text.toString().toFloat()
                }

            if(pageNum<(numPager-1))
            {
                pageNum++
                "${pageNum*5+1} - ${pageNum*5+numPages[pageNum]} de ${list.size} entradas".also { binding.tvPager.text = it }
                limitedTable(tl1,tl2,pageNum)
            }



        }

    }
    private fun addElements(list:List<SkuDetail>){
        list.forEach { category.add(it.category!!)
                            date.add(getDate(it.dateCreation))
                            product.add(it.nameProduct)
                            subCategory.add(it.subCategory!!)
                            inventory.add(it.inventory)
                            price.add(it.price)
                            idSkuDetail.add(it.id)
                            stockSaved.add(it.stock)
                            exhiSave.add(it.exhibition)
                            newPrice.add(it.newPrice)
        }
    }

    private fun removeArray(){
        product.removeAll(product)
        date.removeAll(date)
        category.removeAll(category)
        subCategory.removeAll(subCategory)
        inventory.removeAll(inventory)
        price.removeAll(price)
        idSkuDetail.removeAll(idSkuDetail)
        stockSaved.removeAll(stockSaved)
        exhiSave.removeAll(exhiSave)
        newPrice.removeAll(newPrice)
    }

    private fun getDate(text: String) =  formatter.format(Date.from(Instant.parse(text)))

    private fun textViewStyle(textView: TextView, text:String){
        textView.text =text
        textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTextAll))
        textView.height=60f.toDips().toInt()
        textView.setPadding(15f.toDips().toInt())
        textView.gravity = Gravity.START or Gravity.CENTER_VERTICAL
    }

    private fun showConfirmDialog(type:String){
        val dialog = ConfirmDialogFragment()
        dialog.show(childFragmentManager, "dialog")
        dialog.listener = object  : ConfirmDialogFragment.Callback{
            override fun onClick() {
                for (data in mapCheckStock)
                {
                    val stock =data.value
                    val exhibition = mapCheckEx[data.key]
                    var price = mapEdPrice[data.key]
                    if(price==null)
                        price=0.0f
                    if (exhibition != null) {
                            if (stock != null) {
                                skuViewModel.updateSkuDetail(data.key,stock,exhibition,price)
                            }
                    }
                }

                skuViewModel.updateStateSku(idSkuUpdate,"En proceso",type,latitude,longitude)

            }
        }

    }

    private fun showConfirmSyncDialog(){
        val dialog = ConfirmSyncSkuDialogFragment()
        dialog.listener = object  : ConfirmSyncSkuDialogFragment.Callback{
            override fun onClick() {
               skuViewModel.syncSku(idSkuUpdate)

            }

            override fun onBack() {
               activity?.onBackPressed()
            }

        }
        dialog.show(childFragmentManager,"")
    }

    private fun showFilterDialog(data:ArrayList<String>){
        val dialog = FilterSkuDialogFragment.newInstance(data)
        dialog.show(childFragmentManager, "dialog")
        dialog.listener = object : FilterSkuDialogFragment.Callback{
            override fun onFilter(list:ArrayList<String>) {
                if(list.size==0) {
                    skuViewModel.getSku(1)
                    showTable()
                }
                else
                    showTableFilter(list)
            }
        }
    }

    private fun showObservationDialog(id:String){
        val dialog = ObservationsDialogFragment.newInstance(id,this)
        dialog.show(childFragmentManager, "dialog")
        dialog.listener = object : ObservationsDialogFragment.Callback{
            override fun onClick(id:String,count:Int) {
                for(btn in btnObs)
                {
                    if (btn.tag==id)
                    {
                        when(count)
                        {
                            1-> ("$count Observacion").also { btn.text = it }
                            else -> ("$count Observaciones").also { btn.text = it }
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