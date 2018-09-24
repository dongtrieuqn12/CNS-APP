package com.example.user.myfinalprojectssc_ver1.Retrofit2;


import com.example.user.myfinalprojectssc_ver1.Model.Categories.Categories;
import com.example.user.myfinalprojectssc_ver1.Model.DeleteInvoices;
import com.example.user.myfinalprojectssc_ver1.Model.GetAllInvoices.AllInvoices;
import com.example.user.myfinalprojectssc_ver1.Model.GetAllInvoices.getAllDataInvoices;
import com.example.user.myfinalprojectssc_ver1.Model.Products.Product;
import com.example.user.myfinalprojectssc_ver1.Model.RequestInvoices.RequestCreateInvoices;
import com.example.user.myfinalprojectssc_ver1.Model.ReturnInvoices.ReturnInvoices;
import com.example.user.myfinalprojectssc_ver1.Model.UpdateInvoices.UpdateInvoices;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DongTrieu on 07/2018.
 */

//Các phương thức gởi nhận lên server(GET,POST,PUT,...)
public interface SOService {


    //Lấy sản phẩm
    @Headers({"Retailer:taphoad","Content-Type: application/json"})
    @GET("products?includeInventory=true&pageSize=100")
    Call<Product> getAnswers(@Header("Authorization") String AccessToken, @Query("categoryId") String categoryId,@Query("branchIds") String branchIds);


    //Lấy tổng Hóa đơn của HS.
//    @Headers("Retailer:taphoab")
//    @GET("invoices?branchId=68924&customerCode=c4830689")
//    Call<Invoices> getInvoicesAnswers(@Header("Authorization") String AccessToken,@Query("lastModifiedFrom") String lastModifiedFrom);


    //Tạo hóa đơn
    @Headers({"Retailer:taphoad","Content-Type: application/json"})
    @POST("invoices")
    Call<ReturnInvoices> getReturnInvoicesAfterCreate(@Header("Authorization") String AccessToken, @Body RequestCreateInvoices body);


    //Lấy tất cả hóa đơn trong ngày
    @Headers("Retailer:taphoad")
    @GET("invoices")
    Call<AllInvoices> getReturnAllInvoicesInday(@Header("Authorization") String AccessToken, @Query("branchId") String branchId, @Query("currentItem") int currentItem, @Query("lastModifiedFrom") String lastModifiedFrom);

    @Headers("Retailer:taphoad")
    @GET("invoices?pageSize=100")
    Call<AllInvoices> getReturnAllInvoices(@Header("Authorization") String AccessToken, @Query("branchId") String branchId, @Query("currentItem") int currentItem);


    //get loai hang hoa
    @Headers({"Retailer:taphoad","Content-Type: application/json"})
    @GET("categories")
    Call<Categories> getReturnCategories(@Header("Authorization") String Authorization);

    //query hóa đơn
    @Headers({"Retailer:taphoad","Content-Type: application/json"})
    @GET("invoices?pageSize=100&includeOrderDelivery=true")
    Call<AllInvoices> getReturnInvoicesCustom(@Header("Authorization") String AccessToken, @Query("branchId") String branchId, @Query("currentItem") int currentItem, @Query("lastModifiedFrom") String lastModifiedFrom, @Query("toDate") String toDate);

    //query hóa đơn demo
    @Headers({"Retailer:taphoad","Content-Type: application/json"})
    @GET
    Call<AllInvoices> ReturnInvoices(@Header("Authorization") String AccessToken);


    @Headers({"Retailer:taphoad","Content-Type: application/json"})
    @GET("invoices/{id}")
    Call<getAllDataInvoices> ReturnDataInvoicesSelected(@Header("Authorization") String AccessToken, @Path("id") String idInvoices);

    @Headers({"Retailer:taphoad","Content-Type: application/json"})
    @HTTP(method = "DELETE", path = "invoices",hasBody = true)
    Call<ReturnInvoices> DeleteInvoice(@Header("Authorization") String AccessToken, @Body DeleteInvoices paramdelete);

    @Headers({"Retailer:taphoad","Content-Type: application/json"})
    @PUT("invoices/{id}")
    Call<ReturnInvoices> RETURN_INVOICES_CALL(@Header("Authorization") String AccessToken, @Path("id") String idInvoices, @Body UpdateInvoices updateInvoices);
}
