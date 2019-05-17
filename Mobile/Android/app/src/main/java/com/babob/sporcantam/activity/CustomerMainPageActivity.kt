package com.babob.sporcantam.activity

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.babob.sporcantam.R
import com.babob.sporcantam.adapter.RecyclerCustomerItemAdapter
import com.babob.sporcantam.item.Item
import com.babob.sporcantam.utility.*
import kotlinx.android.synthetic.main.activity_customer_main_page.*

class CustomerMainPageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    lateinit var dataset:ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_main_page)

        title = "Items"

        dataset = arrayListOf()

        viewManager = LinearLayoutManager(this)
        viewAdapter = RecyclerCustomerItemAdapter(dataset, this)


        recyclerView = findViewById<RecyclerView>(R.id.recycelerCustomerItemsView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter


        }

        button_showShoppingCart.setOnClickListener { ActivityOpenerUtil.openShoppingCartActivity(this) }
        button_updateCustomerProfile.setOnClickListener { ActivityOpenerUtil.openUpdateCustomerInfoActivity(this) }
    }

    override fun onResume() {
        super.onResume()
        AsyncUtil{
            updateList()
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

    }

    fun updateList(){
        dataset = JsonUtil.getItemResponseToList(
                HttpUtil.sendPoststr(
                        "","${getString(R.string.base_url)}/item/getItems", SessionUtil.getSessionId(this)!!))

        //viewAdapter = RecyclerCallViewAdapter(dataset)
        runOnUiThread {
            (recyclerView.adapter as RecyclerCustomerItemAdapter).dataset = dataset
            recyclerView.adapter.notifyDataSetChanged()
        }
    }
}
