package com.github.githubusersearch


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.githubusersearch.databinding.ActivityMainBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: GithubUserAdapter
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "main")

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = GithubUserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : GithubUserAdapter. OnItemClickCallback {
        override fun onItemClicked(data: User){
            Intent(this@MainActivity, DetailActivity::class.java).also {
                showLoading(true)
                it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                it.putExtra(DetailActivity.EXTRA_ID, data.id)
                it.putExtra(DetailActivity.EXTRA_URL, data.avatar_url)
                startActivity(it)
                showLoading(false)
            }

        }
        })
        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)
        val pref = MainPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, MainViewModelFactory(pref)).get(MainViewModel::class.java)

        switchTheme.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            mainViewModel.getThemeSettingsMain().observe(this) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
                mainViewModel.saveThemeSettingMain(isChecked)
            }

        }
        //viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = adapter

        binding.btnSearch.setOnClickListener {
            searchUser()
        }

        binding.etQuery.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                searchUser()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        viewModel.getSearchUsers().observe(this)
        {
            if (it!= null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

       private fun searchUser() {
        val query = binding.etQuery.text.toString()
        if (query.isEmpty()) return
        showLoading(true)
        viewModel. setSearchUsers(query)
    }


    private fun showLoading(state: Boolean) {
        if (state
        ) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_menu -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
                }
            R.id.setting_page -> {

                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }


}
