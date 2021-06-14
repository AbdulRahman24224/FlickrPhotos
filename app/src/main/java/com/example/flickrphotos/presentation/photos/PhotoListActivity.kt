package com.example.flickrphotos.presentation.photos


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flickrphotos.R
import com.example.flickrphotos.databinding.ActivityPhotosListBinding
import com.example.flickrphotos.domain.engine.SendSingleItemListener
import com.example.flickrphotos.domain.engine.logDebug
import com.example.flickrphotos.domain.engine.snack
import com.example.flickrphotos.domain.engine.toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins


class PhotoListActivity : AppCompatActivity() {

    lateinit var binding: ActivityPhotosListBinding
    val disposables = CompositeDisposable()

    val viewModel by lazy { ViewModelProvider(this).get(PhotosViewModel::class.java) }
    private lateinit var rvAdapter: PhotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_photos_list)

        setUI()
        observe()

        viewModel.retrievePhotos(this)

        RxJavaPlugins.setErrorHandler { throwable -> throwable.message?.logDebug() }
    }

    private fun observe() {

        viewModel.retrieveProgress.observe(this, Observer {

            if (viewModel.pageLD.value == 1)
                binding.progressBar.visibility = if (it!!) View.VISIBLE else View.GONE
        })

        viewModel.ReposResult.observe(this, Observer {

            if (it!!.isNotEmpty()) with(viewModel.ReposResult.value) {
                binding.rvPhotos.adapter?.notifyDataSetChanged()

            }
        })

        viewModel.toastText
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it?.toast(this) }
            .also { disposables.add(it) }

        viewModel.snackLD.observe(this, Observer {
            if (!it.isNullOrBlank()) it?.snack(binding.root)

        })
    }

    private fun setUI() {
        binding.apply {
            rvPhotos.apply {
                rvAdapter = PhotosAdapter(
                    viewModel.ReposResult,
                    SendSingleItemListener {
                        val resultIntent =
                            Intent(this@PhotoListActivity, PhotoDetailsActivity::class.java)
                        val bundle = Bundle()
                        bundle.putParcelable(PHOTO_OBJECT, it)
                        resultIntent.putExtra(BUNDLE, bundle)
                        startActivity(resultIntent)
                    }
                )

                layoutManager =
                    LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = rvAdapter

                addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (viewModel.pageLD.value == -1) return

                        val itemsCount = recyclerView.layoutManager?.itemCount
                        val lastVisibleItem =
                            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                        if (itemsCount == lastVisibleItem + 3 && dy > 0) {
                            if (dy >= 0) {
                                val linearLayoutManager =
                                    recyclerView.layoutManager as LinearLayoutManager

                                if (viewModel.retrieveProgress.value?.not() == true) {

                                    viewModel.retrievePhotos(context)

                                }
                            }
                        }
                    }
                })
            }

            refreshList.setOnRefreshListener {
                // clearing database data and retrieve new data
                viewModel.clearPhotos()
                refreshList.isRefreshing = false
                viewModel.ReposResult.postValue(mutableListOf())
                viewModel.pageLD.postValue(1)
                viewModel.retrievePhotos(this@PhotoListActivity)
            }
        }
    }


    companion object {
        const val BUNDLE = "BUNDLE"
        const val PHOTO_OBJECT = "PHOTO_OBJECT"
    }

}
