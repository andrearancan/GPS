# CalculatorApi

All URIs are relative to *http://192.168.1.9:8080/v1*

Method | HTTP request | Description
------------- | ------------- | -------------
[**compute**](CalculatorApi.md#compute) | **GET** /calculator/{latitude}/{longitude} | calcolatrice semplice


<a name="compute"></a>
# **compute**
> String compute(latitude, longitude)

calcolatrice semplice

calcola i parametri delle gaussiane che descrivono la mappa

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.CalculatorApi;


CalculatorApi apiInstance = new CalculatorApi();
String latitude = "latitude_example"; // String | 
String longitude = "longitude_example"; // String | 
try {
    String result = apiInstance.compute(latitude, longitude);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CalculatorApi#compute");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **latitude** | **String**|  |
 **longitude** | **String**|  |

### Return type

**String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain

