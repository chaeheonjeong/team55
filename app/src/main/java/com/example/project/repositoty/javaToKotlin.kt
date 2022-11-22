//package com.example.project.repositoty
//
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.target.SimpleTarget
//import android.graphics.Bitmap
//import com.bumptech.glide.request.transition.Transition
//
//class javaToKotlin {
//    fun a() {
//        Glide.with(getContext().getApplicationContext())
//            .asBitmap()
//            .load(path)
//            .into(object : SimpleTarget<Bitmap?>() {
//                override fun onResourceReady(
//                    bitmap: Bitmap,
//                    transition: Transition<in Bitmap>?
//                ) {
//                    val w = bitmap.width
//                    val h = bitmap.height
//                    // mImageView.setImageBitmap(bitmap);
//                }
//            })
//    }
//}