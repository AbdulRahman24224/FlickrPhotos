package com.example.flickrphotos.presentation.photos


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.flickrphotos.databinding.ItemHomeBannerBinding
import com.example.flickrphotos.databinding.ItemHomePhotoBinding
import com.example.flickrphotos.domain.engine.SendSingleItemListener
import com.example.flickrphotos.entities.PhotoModel


const val VIEW_TYPE_ITEM = 1
const val VIEW_TYPE_BANNER = 0

class DataViewHolder(val binding: ItemHomePhotoBinding) : CustomViewHolder(binding.root) {

    fun bind(photo: PhotoModel, sendPhotoItem: SendSingleItemListener<PhotoModel>) {
        binding.apply {
            photoModel = photo
            clickListener = sendPhotoItem

            card.setOnClickListener {
                sendPhotoItem.sendItem(photo)
            }

            executePendingBindings()
        }

    }

}

class BannerViewHolder(binding: ItemHomeBannerBinding) : CustomViewHolder(binding.root) {

}

open class CustomViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {}


class PhotosAdapter(
    private val photosList: MutableLiveData<MutableList<PhotoModel?>>,
    private val sendPhotoItem: SendSingleItemListener<PhotoModel>,
) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): CustomViewHolder {

        if (viewType == VIEW_TYPE_ITEM) {
            return DataViewHolder(
                ItemHomePhotoBinding.inflate(
                    LayoutInflater.from(parentView.context),
                    parentView,
                    false
                )
            )
        } else {
            return BannerViewHolder(
                ItemHomeBannerBinding.inflate(
                    LayoutInflater.from(parentView.context),
                    parentView,
                    false
                )
            )
        }


    }

    override fun onBindViewHolder(viewHolder: CustomViewHolder, position: Int) {
        if (viewHolder is DataViewHolder) {
            // to avoid missing item on banner index
            val imagePosition = if (position > 5) position - 1 else position
            (viewHolder as DataViewHolder).bind(
                photosList.value!![imagePosition] ?: PhotoModel(),
                sendPhotoItem
            )
        }


    }

    override fun getItemCount() = photosList.value?.size ?: 0

    override fun getItemViewType(position: Int): Int {

        return if (position.rem(5) == 0 && position != 0)
            VIEW_TYPE_BANNER
        else
            VIEW_TYPE_ITEM

    }

}