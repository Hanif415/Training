package com.example.android.marsphotos

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.android.marsphotos.network.MarsPhoto
import com.example.android.marsphotos.overview.MarsApiStatus
import com.example.android.marsphotos.overview.PhotoGridAdapter

/*
* for binding the image
*/
@BindingAdapter("imageUrl")
// the binding function
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        // convert url to uri
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        // change uri to image view using coil
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

/*
* for binding the recycler view
*/
@BindingAdapter("listData")
// binding the recyclerview
fun bindRecyclerView(
    recyclerView: RecyclerView,
    data: List<MarsPhoto>?
) {
    // the adapter
    val adapter = recyclerView.adapter as PhotoGridAdapter
    // submit the adapter
    adapter.submitList(data)
}

/*
* for binding the status
*/
@BindingAdapter("marsApiStatus")
fun bindStatus(
    statusImageView: ImageView,
    status: MarsApiStatus?
) {
    when (status) {
        MarsApiStatus.LOADING -> {
            // change the visibility
            statusImageView.visibility = View.VISIBLE
            // change the image resource
            statusImageView.setImageResource(R.drawable.loading_animation)
        }

        MarsApiStatus.ERROR -> {
            // change the visibility
            statusImageView.visibility = View.VISIBLE
            // change the image resource
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }

        MarsApiStatus.DONE -> {
            // hide the image view because is already done
            statusImageView.visibility = View.GONE
        }
    }
}
