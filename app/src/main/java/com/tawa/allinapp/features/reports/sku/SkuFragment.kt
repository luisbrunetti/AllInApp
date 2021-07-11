package com.tawa.allinapp.features.reports.sku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawa.allinapp.core.platform.BaseFragment
import com.tawa.allinapp.databinding.FragmentSkuBinding
import com.tawa.allinapp.models.Sku
import javax.inject.Inject


class SkuFragment : BaseFragment() {

    private lateinit var binding: FragmentSkuBinding

    @Inject lateinit var skuAdapter: SkuAdapter

    var list = ArrayList<Sku>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSku.layoutManager = LinearLayoutManager(context)
        binding.rvSku.adapter = skuAdapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSkuBinding.inflate(inflater)
        list.add(Sku("7UP 1500ml","22/06/2021","Bebida saborizada","7UP","35","1.00"))
        list.add(Sku("7UP 1500ml","22/06/2021","Bebida saborizada","7UP","35","1.00"))
        list.add(Sku("7UP 1500ml","22/06/2021","Bebida saborizada","7UP","35","1.00"))
        list.add(Sku("7UP 1500ml","22/06/2021","Bebida saborizada","7UP","35","1.00"))
        list.add(Sku("7UP 1500ml","22/06/2021","Bebida saborizada","7UP","35","1.00"))
        list.add(Sku("7UP 1500ml","22/06/2021","Bebida saborizada","7UP","35","1.00"))
        skuAdapter.setData(list)
        return binding.root
    }

}