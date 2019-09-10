package gov.wa.wsdot.android.wsdot.ui.trafficmap.newsrelease

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import dagger.android.support.DaggerFragment
import gov.wa.wsdot.android.wsdot.R
import gov.wa.wsdot.android.wsdot.databinding.NewsReleaseFragmentBinding
import gov.wa.wsdot.android.wsdot.db.travelerinfo.NewsRelease
import gov.wa.wsdot.android.wsdot.di.Injectable
import gov.wa.wsdot.android.wsdot.ui.common.binding.FragmentDataBindingComponent
import gov.wa.wsdot.android.wsdot.ui.common.callback.RetryCallback
import gov.wa.wsdot.android.wsdot.util.AppExecutors
import gov.wa.wsdot.android.wsdot.util.autoCleared
import javax.inject.Inject
import android.content.Intent
import android.net.Uri


class NewsReleaseFragment : DaggerFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var newsReleaseViewModel: NewsReleaseViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<NewsReleaseFragmentBinding>()

    private var adapter by autoCleared<NewsReleaseListAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        newsReleaseViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(NewsReleaseViewModel::class.java)

        val dataBinding = DataBindingUtil.inflate<NewsReleaseFragmentBinding>(
            inflater,
            R.layout.news_release_fragment,
            container,
            false
        )

        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                newsReleaseViewModel.refresh()
            }
        }

        dataBinding.viewModel = newsReleaseViewModel

        binding = dataBinding

        // animation
        sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)

        return dataBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        // pass function to be called on adapter item tap and favorite
        val adapter = NewsReleaseListAdapter(dataBindingComponent, appExecutors) { news -> openNewsRelease(news) }

        this.adapter = adapter

        binding.newsList.adapter = adapter

        // animations
        postponeEnterTransition()
        binding.newsList.viewTreeObserver
            .addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }

        newsReleaseViewModel.news.observe(viewLifecycleOwner, Observer { newsResource ->
            if (newsResource.data != null) {
                adapter.submitList(newsResource.data)
            } else {
                adapter.submitList(emptyList())
            }
        })
    }

    private fun openNewsRelease(newsRelease: NewsRelease){
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(newsRelease.link)))
    }
}