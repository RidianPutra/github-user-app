package com.ridianputra.githubuserapp.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.ridianputra.githubuserapp.viewmodel.DetailViewModel
import com.ridianputra.githubuserapp.R
import com.ridianputra.githubuserapp.adapter.SectionsPagerAdapter
import com.ridianputra.githubuserapp.databinding.ActivityDetailBinding
import com.ridianputra.githubuserapp.preferences.MainViewModel
import com.ridianputra.githubuserapp.preferences.SettingPreferences
import com.ridianputra.githubuserapp.preferences.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_users)

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)
        username?.let { detailViewModel.setDetailUser(it) }

        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        detailViewModel.getDetailUser().observe(this) {
            binding.apply {
                nameDetail.text = if(it.name.isNullOrBlank()) getString(R.string.empty_data) else it.name
                usernameDetail.text = if(it.login.isNullOrBlank()) getString(R.string.empty_data) else it.login
                locationDetail.text = if(it.location.isNullOrBlank()) getString(R.string.empty_data) else it.location
                companyDetail.text = if(it.company.isNullOrBlank()) getString(R.string.empty_data) else it.company
                repositoryValue.text = it.publicRepos.toString()
                followersValue.text = it.followers.toString()
                followingValue.text = it.following.toString()
                Glide.with(this@DetailActivity)
                    .load(it.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(avatarDetail)
            }
        }

        var isChecked = false
        detailViewModel.viewModelScope.launch {
            val result = detailViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                isChecked = if (result) {
                    binding.favoriteFab.setImageDrawable(ContextCompat.getDrawable(binding.favoriteFab.context, R.drawable.ic_favorited_24))
                    true
                } else {
                    binding.favoriteFab.setImageDrawable(ContextCompat.getDrawable(binding.favoriteFab.context, R.drawable.ic_favorite_24))
                    false
                }
            }
        }

        binding.favoriteFab.setOnClickListener {
            isChecked = !isChecked
            if(isChecked) {
                binding.favoriteFab.setImageDrawable(ContextCompat.getDrawable(binding.favoriteFab.context, R.drawable.ic_favorited_24))
                detailViewModel.insertUser(id, username!!, avatarUrl!!)
                Snackbar.make(it, R.string.insert_text, Snackbar.LENGTH_SHORT).show()
            } else {
                binding.favoriteFab.setImageDrawable(ContextCompat.getDrawable(binding.favoriteFab.context, R.drawable.ic_favorite_24))
                detailViewModel.deleteUser(id)
                Snackbar.make(it, R.string.delete_text, Snackbar.LENGTH_SHORT).show()
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.actionbar_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.setting -> {
                if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    mainViewModel.saveThemeSetting(false)
                } else {
                    mainViewModel.saveThemeSetting(true)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}