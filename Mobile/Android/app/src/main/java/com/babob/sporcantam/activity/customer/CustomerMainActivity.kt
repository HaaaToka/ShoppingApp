package com.babob.sporcantam.activity.customer

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.babob.sporcantam.R
import com.babob.sporcantam.adapter.RecyclerCustomerItemAdapter
import com.babob.sporcantam.item.Item
import com.babob.sporcantam.utility.*
import kotlinx.android.synthetic.main.content_customer_main.*
import kotlinx.android.synthetic.main.nav_header_admin_nav_main_page.*


class CustomerMainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    lateinit var dataset:ArrayList<Item>
    lateinit var navView: NavigationView
    var isLogged:Boolean = false

    var condition:String=""
    var isQuery:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_main)
        isLogged = SessionUtil.isLogged(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navMenu = navView.menu
        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        updateMenuItems()


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

        navView.setNavigationItemSelectedListener(this)


        simpleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            override fun onQueryTextSubmit(query: String): Boolean {
                //UserFeedback.show("SearchOnQueryTextSubmit: " + query);

                Log.d("Customer Main Page", "query: $query")
                isQuery=true
                onResume()

                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                //SPANLog.d( "HEY HEY" , "$s")
                //TODO: sorry bro, below code is very stupid
                // carpiya basinca basa donsun die :(
                if(s.length==0) {
                    condition=""
                    isQuery=false
                    onResume()
                }
                return false
            }
        })


    }

    override fun onResume() {
        super.onResume()
        updateMenuItems()
        AsyncUtil{
            updateList(condition,isQuery)
            val bal = getBalance()
            runOnUiThread { textView_header_balance.text = bal }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    fun getBalance():String{
        val response = HttpUtil.sendPoststr(
                "", "${getString(R.string.base_url)}/customer/getBalance", SessionUtil.getSessionId(this)!!)
        return if(! response.isEmpty()){
            response
        }
        else {
            "0"
        }
    }
    private fun updateMenuItems(){
        val navMenu = navView.menu
        isLogged = SessionUtil.isLogged(this)
        if(isLogged){
            navMenu.findItem(R.id.nav_login).isVisible = false
        } else {
            navMenu.findItem(R.id.nav_logout).isVisible = false
            navMenu.findItem(R.id.nav_shopping_cart).isVisible = false
            navMenu.findItem(R.id.nav_update_profile).isVisible = false
            navMenu.findItem(R.id.nav_add_balance).isVisible = false
            navMenu.findItem(R.id.nav_view_history).isVisible = false
            navMenu.findItem(R.id.nav_view_order_history).isVisible = false
            navMenu.findItem(R.id.nav_login).isVisible = true
        }
    }

    fun updateList(condition:String, query:Boolean){

        if(query){
            dataset = JsonUtil.getItemResponseToList(HttpUtil.sendPoststr(
                            UrlParamUtil.searchItem(simpleSearchView.query.toString()),"${getString(R.string.base_url)}/item/searchItems",SessionUtil.getSessionId(this)!!))
        }
        else{
            if(condition=="")
                dataset = JsonUtil.getItemResponseToList(HttpUtil.sendPoststr(
                        condition,"${getString(R.string.base_url)}/item/getItems", SessionUtil.getSessionId(this)!!))
            else{
                dataset = JsonUtil.getItemResponseToList(HttpUtil.sendPoststr(
                        UrlParamUtil.categorySearchItem(condition),"${getString(R.string.base_url)}/item/category/"+condition,SessionUtil.getSessionId(this)!!))
            }
        }
        this.isQuery=false
        this.condition =""
        //viewAdapter = RecyclerCallViewAdapter(dataset)
        runOnUiThread {
            recyclerView.removeAllViews()
            (recyclerView.adapter as RecyclerCustomerItemAdapter).dataset = dataset
            recyclerView.adapter.notifyDataSetChanged()
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.customer_main, menu)
        return true
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_update_profile -> {
                ActivityOpenerUtil.openUpdateCustomerInfoActivity(this)
            }
            R.id.nav_shopping_cart -> {
                ActivityOpenerUtil.openShoppingCartActivity(this)
            }
            R.id.nav_add_balance -> {
                ActivityOpenerUtil.openAddBalanceActivity(this)
            }
            R.id.nav_view_history -> {
                ActivityOpenerUtil.openViewHistoryActivity(this)
            }
            R.id.nav_view_order_history -> {
                ActivityOpenerUtil.openViewOrdersActivity(this)
            }
            R.id.nav_logout -> {
                SessionUtil.logOut(this)
                updateMenuItems()
            }
            R.id.nav_login -> {
                ActivityOpenerUtil.openLoginActivity(this)
            }

            R.id.nav_running->{
                condition="running"
                onResume()
            }
            R.id.nav_clothes->{
                condition="clothes"
                onResume()
            }
            R.id.nav_fitness->{
                condition="fitness"
                onResume()
            }
            R.id.nav_hiking->{
                condition="hiking"
                onResume()
            }
            R.id.nav_ski->{
                condition="ski"
                onResume()
            }
            R.id.nav_snowboard->{
                condition="snowboard"
                onResume()
            }
            R.id.nav_soccer->{
                condition="soccer"
                onResume()
            }
            R.id.nav_basketball->{
                condition="basketball"
                onResume()
            }
            R.id.nav_swimming->{
                condition="swimming"
                onResume()
            }
            R.id.nav_cycle->{
                condition="cycling"
                onResume()
            }
            R.id.nav_tennis->{
                condition="tennis"
                onResume()
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
