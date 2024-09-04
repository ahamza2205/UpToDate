package com.example.newsapiapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapiapp.R
import com.example.newsapiapp.adapters.ArticleAdapter
import com.example.newsapiapp.adapters.SavedArticleAdapter
import com.example.newsapiapp.mvvm.NewsDatabase
import com.example.newsapiapp.mvvm.NewsRepo
import com.example.newsapiapp.mvvm.NewsViewModel
import com.example.newsapiapp.mvvm.NewsViewModelFac


class FragmentSavedNews : Fragment() , MenuProvider {

    lateinit var viewModel: NewsViewModel
    lateinit var savedArticleAdapter: SavedArticleAdapter
    lateinit var rv: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Saved Articles"


       // initializing menu
        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.CREATED)
        setHasOptionsMenu(true)

        // initializing recycler view
        savedArticleAdapter = SavedArticleAdapter()
        rv = view.findViewById(R.id.rvSavedNews)


        // initializing view model
        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepo(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

            viewModel.getSavedNews.observe(viewLifecycleOwner , Observer {

                savedArticleAdapter.setlist(it)
                setUpRecyclerView()

            })
    }

    private fun setUpRecyclerView() {
        rv.apply {
            adapter = savedArticleAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

        menuInflater.inflate(R.menu.menu, menu)

        val searchIcon = menu.findItem(R.id.searchNews)
        val savedIcon = menu.findItem(R.id.savedNewsFrag)

        searchIcon.setVisible(false)
        savedIcon.setVisible(false)
        super.onCreateOptionsMenu(menu, menuInflater)

    }



    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {


        if (menuItem.itemId == R.id.deleteAll){


            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Delete Menu")
            builder.setMessage("Are you sure to delete All saved articles")

            builder.setPositiveButton("Delete All"){dialog, which->


                viewModel.deleteAllArtciles()
                Toast.makeText(context, "Deleted All", Toast.LENGTH_SHORT).show()

                view?.findNavController()?.navigate(R.id.action_fragmentSavedNews_to_fragmentBreakingNews)

            }

            builder.setNegativeButton("Cancel"){dialog, which->

                dialog.dismiss()

            }

            val dialog = builder.create()

            dialog.show()




        }


        return true


    }

}