
package com.example.newsapiapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapiapp.R
import com.example.newsapiapp.adapters.ArticleAdapter
import com.example.newsapiapp.adapters.ItemClicklistner
import com.example.newsapiapp.database.Article
import com.example.newsapiapp.mvvm.NewsDatabase
import com.example.newsapiapp.mvvm.NewsRepo
import com.example.newsapiapp.mvvm.NewsViewModel
import com.example.newsapiapp.mvvm.NewsViewModelFac
import com.example.newsapiapp.wrapper.Resource
import de.hdodenhof.circleimageview.CircleImageView

class FragmentBreakingNews : Fragment(), ItemClicklistner {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: ArticleAdapter
    lateinit var rv: RecyclerView
    lateinit var pb: ProgressBar
    var isClicked: Boolean = false
    var isOpened: Boolean = false
    var addingResponselist = arrayListOf<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_breaking_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Breaking News"

        setHasOptionsMenu(true)

        val sportCat: CircleImageView = view.findViewById(R.id.sportsImage)
        val techCat: CircleImageView = view.findViewById(R.id.techImage)
        val breakingImage: CircleImageView = view.findViewById(R.id.breakingImage)
        val businessCat: CircleImageView = view.findViewById(R.id.businessImage)

        val noWifi: ImageView = view.findViewById(R.id.noWifi)
        val noWifiText: TextView = view.findViewById(R.id.noWifiText)

        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepo(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

        rv = view.findViewById(R.id.rvBreakingNews)
        pb = view.findViewById(R.id.paginationProgressBar)

        val cm = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nInfo = cm.activeNetworkInfo
        if (nInfo != null && nInfo.isConnected) {
            setUpRecyclerView()
            isClicked = true
            loadBreakingNews()
        } else {
            // IF THERE IS NO INTERNET THEN DISPLAY THIS
            noWifi.visibility = View.VISIBLE
            noWifiText.visibility = View.VISIBLE
        }

        val catListener = View.OnClickListener {
            when (it.id) {
                R.id.sportsImage -> {
                    (activity as AppCompatActivity).supportActionBar?.setTitle("Sports")
                    isClicked = true
                    viewModel.getCategory("sports")
                    loadCategoryNews()
                    setUpRecyclerView()
                }

                R.id.techImage -> {
                    (activity as AppCompatActivity).supportActionBar?.setTitle("Tech")
                    isClicked = true
                    viewModel.getCategory("tech")
                    loadCategoryNews()
                    setUpRecyclerView()
                }

                R.id.breakingImage -> {
                    (activity as AppCompatActivity).supportActionBar?.setTitle("Breaking News")
                    isClicked = true
                    loadBreakingNews()
                }

                R.id.businessImage -> {
                    (activity as AppCompatActivity).supportActionBar?.setTitle("Business")
                    isClicked = true
                    viewModel.getCategory("business")
                    loadCategoryNews()
                    setUpRecyclerView()
                }
            }
        }

        techCat.setOnClickListener(catListener)
        breakingImage.setOnClickListener(catListener)
        businessCat.setOnClickListener(catListener)
        sportCat.setOnClickListener(catListener)
    }

    private fun loadCategoryNews() {
        viewModel.categoryNews.observe(this.viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        addingResponselist = newsResponse.articles as ArrayList<Article>
                        newsAdapter.setlist(newsResponse.articles)
                        Log.i("Category News", "Number of fetched items: ${newsResponse.articles.size}")
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.i("Category News", message.toString())
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun loadBreakingNews() {
        viewModel.breakingNews.observe(viewLifecycleOwner){ response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        addingResponselist = newsResponse.articles as ArrayList<Article>
                        newsAdapter.setlist(newsResponse.articles)
                        Log.i("Breaking News", "Number of fetched items: ${newsResponse.articles.size}")
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.i("Breaking News", message.toString())
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    fun showProgressBar() {
        pb.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        pb.visibility = View.INVISIBLE
    }

    private fun setUpRecyclerView() {
        newsAdapter = ArticleAdapter()
        newsAdapter.setItemClickListener(this)
        rv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onItemClicked(position: Int, article: Article) {
        // GOING TO ANOTHER FRAGMENT
        val action = FragmentBreakingNewsDirections.actionFragmentBreakingNewsToFragmentArticle()
        view?.findNavController()?.navigate(action)
        Toast.makeText(context, "check ${article.title}", Toast.LENGTH_SHORT).show()
    }
}
