package com.tawa.allinapp.features.init

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tawa.allinapp.R
import com.tawa.allinapp.core.extensions.inflate
import com.tawa.allinapp.core.extensions.loadFromUrl
import com.tawa.allinapp.features.auth.Company
import com.tawa.allinapp.features.movies.MovieView
import kotlinx.android.synthetic.main.row_movie.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class CompanyAdapater


@Inject constructor(context: Context, resource: Int, objects: MutableList<Company>) :
    ArrayAdapter<Company>(context, resource, objects) {


    override fun getItem(position: Int): Company? {
        return super.getItem(position)


    }





}