package com.babob.sporcantam.utility

import android.util.Log
import com.babob.sporcantam.item.Customer
import com.babob.sporcantam.item.Item
import com.babob.sporcantam.item.Order
import com.babob.sporcantam.item.Seller
import org.json.JSONException
import org.json.JSONObject

class JsonUtil {
    companion object {
        fun handleStringResponse(response: String): Boolean {
            try {
                val jsonObj = JSONObject(response)
                return jsonObj.get("success") as Boolean
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return false
        }

        fun generalServerResponseToList(response: String):List<String>{
            try {
                val jsonObj = JSONObject(response)
                val res1 = jsonObj.get("success") as Boolean
                val res2 = jsonObj.get("msg") as String
                return listOf(res1.toString(),res2)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return listOf()
        }

        fun getItemResponseToList(response: String):ArrayList<Item>{
            try {
                val jsonObj = JSONObject(response)
                val retList = arrayListOf<Item>()
                val arr = jsonObj.getJSONArray("items")
                var i = 0
                while (i < arr.length()){
                    val itemJsonObj = arr.getJSONObject(i++)
                    val itemTitle = itemJsonObj.get("item_title") as String
                    val description = itemJsonObj.get("description") as String
                    val price = itemJsonObj.get("price") as Double
                    val seller = itemJsonObj.get("seller") as String
                    val shipping = itemJsonObj.get("shipping_info") as String
                    val stockCount = itemJsonObj.get("stock_count") as Int
                    val category = itemJsonObj.get("category") as String
                    val uuid = itemJsonObj.get("uuid") as String
                    val item = Item(itemTitle, price.toFloat(), seller, description, shipping, stockCount,  category, uuid)
                    retList.add(item)
                    Log.d("Json", itemJsonObj.toString())

                }
                return retList

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return arrayListOf()
        }

        fun getOrderResponseToList(response: String):ArrayList<Order>{
            try {
                val jsonObj = JSONObject(response)
                val retList = arrayListOf<Order>()
                val arr = jsonObj.getJSONArray("orders")
                var i = 0
                while (i < arr.length()){

                    val orderJsonObj = arr.getJSONObject(i++)
                    val orderSellerEmail = orderJsonObj.get("seller_email") as String
                    val orderCustomerEmail = orderJsonObj.get("customer_email") as String
                    val order_id = orderJsonObj.get("order_id") as Int
                    val order = Order(orderSellerEmail,orderCustomerEmail,order_id)
                    retList.add(order)
                    Log.d("Json",orderJsonObj.toString())

                }
                return retList

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return arrayListOf()
        }

        fun getCustomerResponseToList(response: String):ArrayList<Customer>{
            try {
                val jsonObj = JSONObject(response)
                val retList = arrayListOf<Customer>()
                val arr = jsonObj.getJSONArray("customers")
                var i = 0
                while (i < arr.length()){

                    val customerJsonObj = arr.getJSONObject(i++)
                    val customerFirstName = customerJsonObj.get("first_name") as String
                    val customerSecondName = customerJsonObj.get("second_name") as String
                    val customerAddress = customerJsonObj.get("address") as String
                    val customer = Customer(customerFirstName,customerSecondName,customerAddress)
                    retList.add(customer)
                    Log.d("Json",customerJsonObj.toString())

                }
                return retList

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return arrayListOf()
        }

        fun getSellerResponseToList(response: String):ArrayList<Seller>{
            try {
                val jsonObj = JSONObject(response)
                val retList = arrayListOf<Seller>()
                val arr = jsonObj.getJSONArray("seller")
                var i = 0
                while (i < arr.length()){

                    val sellerJsonObj = arr.getJSONObject(i++)
                    val sellerFirstName = sellerJsonObj.get("first_name") as String
                    val sellerSecondName = sellerJsonObj.get("second_name") as String
                    val sellerAddress = sellerJsonObj.get("address") as String
                    val sellerIBAN = sellerJsonObj.get("IBAN") as String
                    val sellerPhone = sellerJsonObj.get("phone_number") as String
                    val sellerEmail = sellerJsonObj.get("email") as String
                    val seller = Seller(sellerFirstName,sellerSecondName,sellerAddress,sellerIBAN,sellerPhone,sellerEmail)
                    retList.add(seller)
                    Log.d("Json",sellerJsonObj.toString())
                }
                return retList

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return arrayListOf()
        }


        fun customerInfoResponseToCustomer(response: String):Customer{
            try{
                val jsonObj = JSONObject(response)
                val customer = Customer()
                customer.firstName = jsonObj.getString("first_name")
                customer.lastName = jsonObj.getString("last_name")
                customer.address = jsonObj.getString("address")
                return customer
            }catch (e: JSONException) {
                e.printStackTrace()
            }
            return Customer()
        }

        fun sellerInfoResponseToSeller(response: String): Seller {
            try{
                val jsonObj = JSONObject(response)
                val seller = Seller()
                seller.firstName = jsonObj.getString("first_name")
                seller.lastName = jsonObj.getString("last_name")
                seller.address = jsonObj.getString("company_address")
                seller.IBAN = jsonObj.getString("iban")
                seller.phoneNumber = jsonObj.getString("phone_number")

                return seller
            }catch (e: JSONException) {
                e.printStackTrace()
            }
            return Seller()
        }

        fun getGenerateSaleReport(response: String):HashMap<String,String>{

            val soldItemCounts:HashMap<String,String> = hashMapOf()

            val categories=ArrayList<String>()
            SpinnerHelperUtil.categoryHelper(categories)

            try {
                val jsonObj = JSONObject(response)

                var i=0
                for(cat in categories) {
                    soldItemCounts[cat.toLowerCase()]=jsonObj.getString(cat.toLowerCase())
                }

            }catch (e: Exception){
                e.printStackTrace()
            }

            return soldItemCounts
        }


    }
}