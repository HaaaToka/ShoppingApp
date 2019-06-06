package com.babob.sporcantam.Admin;

import com.babob.sporcantam.Customer.Customer;
import com.babob.sporcantam.Customer.CustomerRepository;
import com.babob.sporcantam.Item.Item;
import com.babob.sporcantam.Item.ItemRepository;
import com.babob.sporcantam.Order.Order;
import com.babob.sporcantam.Order.OrderRepository;
import com.babob.sporcantam.Seller.Seller;
import com.babob.sporcantam.Seller.SellerRepository;
import com.babob.sporcantam.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(path="/admin")
public class AdminController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @RequestMapping(method=POST,path="/login")
    public @ResponseBody
    Response adminLogin(@RequestParam String email, @RequestParam String password,
                           @CookieValue(name = "JSESSIONID") String sessionID){
        try{
            Admin admin = adminRepository.findByEmail(email).iterator().next(); //get first (and oly) customer
            if(passwordEncoder.matches(password,admin.getPassword()))
            {
                admin.setSessionID(sessionID);
                adminRepository.save(admin);
                return new Response("Login successful",true);
            }

            else{
                return new Response("Incorrect password",false);
            }

        }
        catch(Exception e){
            return new Response("No such admin",false);
        }
    }
    @RequestMapping(method=POST,path="/sale/confirm")
    public @ResponseBody
    Response confirmSale(@RequestParam Long saleID,
                        @CookieValue(name = "JSESSIONID") String sessionID){
        try{
            Admin admin = adminRepository.findBySessionID(sessionID).iterator().next();
            //get first (and oly) customer
            Order order = orderRepository.findByOrderID(saleID).iterator().next();
            order.setConfirmed(true);
            orderRepository.save(order);
            return new Response("Order confirmed",true);
        }
        catch(Exception e){
            return new Response("No such admin",false);
        }
    }
    @RequestMapping(method = POST, path = "/seller/update")
    public @ResponseBody
    Response manipulateSeller(@CookieValue(name = "JSESSIONID") String sessionID
            , @RequestParam(value = "seller_email", required = false, defaultValue = " ") String seller_email
            , @RequestParam(value = "password", required = false, defaultValue = " ") String password
            , @RequestParam(value = "first_name", required = false, defaultValue = " ") String first_name
            , @RequestParam(value = "last_name", required = false, defaultValue = " ") String last_name
            , @RequestParam(value = "IBAN", required = false, defaultValue = " ") String IBAN
            , @RequestParam(value = "phone_number", required = false, defaultValue = " ") String phone_number
            , @RequestParam(value = "company_address", required = false, defaultValue = " ") String company_address) {


        try {
            Admin admin = adminRepository.findBySessionID(sessionID).iterator().next();
            if(admin == null) {
                return new Response("Cannot update seller info!", false);
            }
            Seller seller = sellerRepository.findByEmail(seller_email).iterator().next();
            if (!password.equals(" "))
                seller.setPassword(passwordEncoder.encode(password));
            if (!first_name.equals(" "))
                seller.setFirst_name(first_name);
            if (!last_name.equals(" "))
                seller.setLast_name(last_name);
            if (!IBAN.equals(" "))
                seller.setIBAN(IBAN);
            if (!company_address.equals(" "))
                seller.setCompany_address(company_address);
            if (!phone_number.equals(" "))
                seller.setPhone_number(phone_number);
            sellerRepository.save(seller);
            return new Response("Seller information updated succesfully.", true);
        } catch (Exception e) {
            return new Response("Cannot update seller info!", false);
        }
    }
    @RequestMapping(method=POST,path ="/customer/update")
    public @ResponseBody
    Response updateCustomerInfo(@CookieValue(name = "JSESSIONID") String sessionID
            ,@RequestParam(value = "customer_email", required = false, defaultValue = " ") String customer_email
            ,@RequestParam(value="password", required = false, defaultValue=" ") String password
            ,@RequestParam(value="first_name", required = false, defaultValue=" ") String first_name
            ,@RequestParam(value="last_name", required = false, defaultValue=" ") String last_name
            ,@RequestParam(value="address", required = false, defaultValue=" ") String address) {

        try{
            Admin admin = adminRepository.findBySessionID(sessionID).iterator().next();
            if(admin == null) {
                return new Response("Cannot update seller info!", false);
            }
            Customer customer = customerRepository.findByEmail(customer_email).iterator().next();
            if(password!= " ")
                customer.setPassword(passwordEncoder.encode(password));
            if(first_name!= " ")
                customer.setFirst_name(first_name);
            if(last_name!= " ")
                customer.setLast_name(last_name);
            if(address!= " ")
                customer.setAddress(address);
            customerRepository.save(customer);
            return new Response("Customer information updated succesfully.",true);

        }
        catch (Exception e){
            return new Response("Cannot update customer info!",false);
        }
    }

    @RequestMapping(method = POST, path = "/item/update")
    public @ResponseBody
    Response updateItemInfo(@CookieValue(name = "JSESSIONID") String sessionID
            ,@RequestParam(value = "item_UUID", required = false, defaultValue = " ") String UUID
            , @RequestParam(value = "item_title", required = false, defaultValue = " ") String item_title
            , @RequestParam(value = "price", required = false, defaultValue = "-1.0") float price
            , @RequestParam(value = "description", required = false, defaultValue = " ") String description
            , @RequestParam(value = "stock_count", required = false, defaultValue = "-1") int stock_count
            , @RequestParam(value = "shipping_info", required = false, defaultValue = " ") String shipping_info) {


        try{
            Admin admin = adminRepository.findBySessionID(sessionID).iterator().next();
            if(admin == null) {
                return new Response("Cannot update seller info!", false);
            }
            Item item = itemRepository.findByUUID(UUID).iterator().next();
            if(!item_title.equals(" "))
                item.setItem_title(item_title);
            if(price != -1.0)
                item.setPrice(price);
            if(stock_count != -1)
                item.setStock_count(stock_count);
            if(!description.equals(" "))
                item.setDescription(description);
            if(!shipping_info.equals(" "))
                item.setShipping_info(shipping_info);
            itemRepository.save(item);
            return new Response("Item information updated succesfully.",true);

        }catch (Exception e){
            return new Response("Cannot update item info!",false);
        }
    }

}