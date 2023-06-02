package com.example.recylerviewkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.widget.SearchView

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var newRecylerview : RecyclerView
    private lateinit var newArrayList : ArrayList<News>
    private lateinit var tempArrayList : ArrayList<News>
    lateinit var imageId : Array<Int>
    lateinit var heading : Array<String>
    lateinit var news : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageId = arrayOf(
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e,





        )

        heading = arrayOf(
                "wisata sawah ",
                "pura Besakih ",
                " Pura Ulun",
                "air terjun",
                "GOA gajah"




        )

        news = arrayOf(

                getString(R.string.news_a),
                getString(R.string.news_b),
                getString(R.string.news_c),
                getString(R.string.news_d),
                getString(R.string.news_e)





        )





        newRecylerview =findViewById(R.id.recyclerView)
        newRecylerview.layoutManager = LinearLayoutManager(this)
        newRecylerview.setHasFixedSize(true)


        newArrayList = arrayListOf<News>()
        tempArrayList = arrayListOf<News>()
        getUserdata()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_item,menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()){

                    newArrayList.forEach {

                        if (it.heading.toLowerCase(Locale.getDefault()).contains(searchText)){


                            tempArrayList.add(it)

                        }

                    }

                    newRecylerview.adapter!!.notifyDataSetChanged()

                }else{

                    tempArrayList.clear()
                    tempArrayList.addAll(newArrayList)
                    newRecylerview.adapter!!.notifyDataSetChanged()

                }


                return false

            }


        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun getUserdata() {

        for(i in imageId.indices){

            val news = News(imageId[i],heading[i])
            newArrayList.add(news)

        }

        tempArrayList.addAll(newArrayList)


        val adapter = MyAdapter(tempArrayList)
        val swipegesture = object : SwipeGesture(this){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {

                val from_pos = viewHolder.adapterPosition
                val to_pos = target.adapterPosition

                Collections.swap(newArrayList,from_pos,to_pos)
                adapter.notifyItemMoved(from_pos,to_pos)

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when(direction){

                    ItemTouchHelper.LEFT ->{

                        adapter.deleteItem(viewHolder.adapterPosition)
                    }

                    ItemTouchHelper.RIGHT -> {

                        val archiveItem = newArrayList[viewHolder.adapterPosition]
                        adapter.deleteItem(viewHolder.adapterPosition)
                        adapter.addItem(newArrayList.size,archiveItem)

                    }

                }

            }

        }
        val touchHelper = ItemTouchHelper(swipegesture)
        touchHelper.attachToRecyclerView(newRecylerview)
        newRecylerview.adapter = adapter
        adapter.setOnItemClickListener(object : MyAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

              //  Toast.makeText(this@MainActivity,"You Clicked on item no. $position",Toast.LENGTH_SHORT).show()

                val intent = Intent(this@MainActivity,NewsActivity::class.java)
                intent.putExtra("heading",newArrayList[position].heading)
                intent.putExtra("imageId",newArrayList[position].titleImage)
                intent.putExtra("news",news[position])
                startActivity(intent)

            }


        })

    }
}