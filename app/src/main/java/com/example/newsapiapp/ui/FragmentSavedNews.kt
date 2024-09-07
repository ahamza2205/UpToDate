package com.example.newsapiapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapiapp.R
import com.example.newsapiapp.adapters.SavedArticleAdapter
import com.example.newsapiapp.mvvm.NewsDatabase
import com.example.newsapiapp.mvvm.NewsRepo
import com.example.newsapiapp.mvvm.NewsViewModel
import com.example.newsapiapp.mvvm.NewsViewModelFac

class FragmentSavedNews : Fragment(), MenuProvider {

    lateinit var viewModel: NewsViewModel
    lateinit var savedArticleAdapter: SavedArticleAdapter
    lateinit var rv: RecyclerView
    var isListEmpty = true

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

        // Initializing menu
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Initializing RecyclerView
        savedArticleAdapter = SavedArticleAdapter()
        rv = view.findViewById(R.id.rvSavedNews)

        // Initializing ViewModel
        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepo(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

        // Observe saved news
        viewModel.getSavedNews.observe(viewLifecycleOwner) {
            isListEmpty = it.isEmpty() // Check if the list is empty
            savedArticleAdapter.setlist(it)
            setUpRecyclerView()

            // Refresh the menu to update visibility based on the list status
            requireActivity().invalidateOptionsMenu()
        }
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
        val deleteAllIcon = menu.findItem(R.id.deleteAll)

        searchIcon.isVisible = false
        savedIcon.isVisible = false
        deleteAllIcon.isVisible = !isListEmpty // Only show if the list is not empty
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.deleteAll) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Menu")
            builder.setMessage("Are you sure to delete all saved articles?")

            builder.setPositiveButton("Delete All") { dialog, _ ->
                viewModel.deleteAllArtciles()
                Toast.makeText(context, "Deleted All", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.navigate(R.id.action_fragmentSavedNews_to_fragmentBreakingNews)
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        return true
    }
}
