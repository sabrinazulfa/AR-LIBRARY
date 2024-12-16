package com.airlibrary.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkUtils {
    
    /**
     * Memeriksa koneksi internet
     * @param context Konteks aplikasi
     * @return true jika terhubung ke internet, false jika tidak
     */
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager != null) {
            // Untuk perangkat Android versi di atas Lollipop
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connectivityManager.getActiveNetwork();
                if (network == null) return false;
                
                NetworkCapabilities capabilities = 
                    connectivityManager.getNetworkCapabilities(network);
                if (capabilities == null) return false;
                
                return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                       (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } 
            // Untuk perangkat Android versi di bawah Lollipop
            else {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }
        
        return false;
    }

    /**
     * Memeriksa koneksi WiFi
     * @param context Konteks aplikasi
     * @return true jika terhubung ke WiFi, false jika tidak
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = connectivityManager.getActiveNetwork();
                if (network == null) return false;
                
                NetworkCapabilities capabilities = 
                    connectivityManager.getNetworkCapabilities(network);
                if (capabilities == null) return false;
                
                return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            } 
            else {
                NetworkInfo networkInfo = 
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        
        return false;
    }

    /**
     * Mendapatkan tipe koneksi internet
     * @param context Konteks aplikasi
     * @return String tipe koneksi (WiFi/Seluler/Tidak Terhubung)
     */
    public static String getConnectionType(Context context) {
        if (!isInternetAvailable(context)) {
            return "Tidak Terhubung";
        }
        
        if (isWifiConnected(context)) {
            return "WiFi";
        }
        
        return "Seluler";
    }
}