package com.assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Switch
import android.widget.TableLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var tableLayout: TabLayout
    lateinit var recycler_view: RecyclerView
    lateinit var viewPager: ViewPager2
    lateinit var searchText: EditText
    lateinit var fab: FloatingActionButton
    var selectedArray = mutableListOf<Item>()
    var searchArray = mutableListOf<Item>()
    var selectedPosition : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //findViewById of the layout elements
        tableLayout = findViewById(R.id.viewpager_tabs)
        recycler_view = findViewById(R.id.recycler_view)
        searchText = findViewById(R.id.searchText)
        fab = findViewById(R.id.fab)


        //clicking on fab floating icon
        fab.setOnClickListener { showBottomSheetDialog() }

        //implemented addTextChangedListener to the search text to do automatic search from the Recycleview data when user type anything

        searchText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0!!.length > 0) {
                    searchFromList(p0.toString())
                }else {
                    val listAdapter = ItemAdapter(selectedArray)
                    recycler_view.adapter = listAdapter
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        //Creating dummy data for Recycleview...

        val itemsList = arrayListOf(
            Item(R.drawable.image1, "Label 1"),
            Item(R.drawable.image2, "Label 2"),
            Item(R.drawable.image3, "Label 3"),
            Item(R.drawable.image1, "Item 1"),
            Item(R.drawable.image2, "Item 2"),
            Item(R.drawable.image3, "Item 3"),
            Item(R.drawable.image1, "Data 1"),
            Item(R.drawable.image2, "Data 2"),
            Item(R.drawable.image3, "Data 3")
            // Add more items as needed
        )
        val itemsList1 = arrayListOf(
            Item(R.drawable.image3, "Label 4"),
            Item(R.drawable.image1, "Label 5"),
            Item(R.drawable.image2, "Label 6"),
            Item(R.drawable.image3, "Item 4"),
            Item(R.drawable.image1, "Item 5"),
            Item(R.drawable.image2, "Item 6"),
            Item(R.drawable.image3, "Data 4"),
            Item(R.drawable.image1, "Data 5"),
            Item(R.drawable.image3, "Data 6")
            // Add more items as needed
        )
        val itemsList2 = arrayListOf(
            Item(R.drawable.image1, "Label 7"),
            Item(R.drawable.image3, "Label 8"),
            Item(R.drawable.image2, "Label 9"),
            Item(R.drawable.image1, "Item 7"),
            Item(R.drawable.image3, "Item 8"),
            Item(R.drawable.image2, "Item 9"),
            Item(R.drawable.image1, "Data 7"),
            Item(R.drawable.image3, "Data 8"),
            Item(R.drawable.image2, "Data 9")
            // Add more items as needed
        )

        //End og creating dummy data...........


        //setting the adapter to the viewpager to show the images......
        viewPager = findViewById(R.id.viewpager_images)
        val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)
        val adapter = ImageAdapter(images)
        viewPager.adapter = adapter

        //attach the tablayout dot with the viewpager
        TabLayoutMediator(tableLayout, viewPager) { tab, position ->
            //tab.text = adapter.getPageTitle(position)
        }.attach()


        //setting the layoutmanager and adapter to the Recycleview
        val layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager

        val listAdapter = ItemAdapter(itemsList)
        recycler_view.adapter = listAdapter


        //add registerOnPageChangeCallback to the viewpager to refresh the Recycleview data when user swipe the viewpager content
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectedPosition = position
                if (position == 0){

                    selectedArray = itemsList
                    val listAdapter = ItemAdapter(selectedArray)
                    recycler_view.adapter = listAdapter

                }else if (position == 1){

                    selectedArray = itemsList1
                    val listAdapter = ItemAdapter(selectedArray)
                    recycler_view.adapter = listAdapter

                }else if (position == 2){

                    selectedArray = itemsList2
                    val listAdapter = ItemAdapter(selectedArray)
                    recycler_view.adapter = listAdapter
                }
            }
        })
    }

    //creating method to search and show it to the list
    fun searchFromList(input : String){
        searchArray.clear()
        selectedArray.forEach {
            if (it.label.contains(input,true)){
                searchArray.add(it)
            }
        }
        val listAdapter = ItemAdapter(searchArray)
        recycler_view.adapter = listAdapter

    }

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        val item_count = bottomSheetView.findViewById<TextView>(/* id = */ R.id.item_count);
        val char_stats = bottomSheetView.findViewById<TextView>(/* id = */ R.id.char_stats);

        val itemList = listOf("apple", "banana", "orange", "blueberry")
        val itemCount = itemList.size
        item_count.text = "Item Count: $itemCount"

        val charStats = getCharacterStatistics(itemList)
        char_stats.text = charStats

        bottomSheetDialog.show()
    }

    private fun getCharacterStatistics(itemList: List<String>): String {
        val charCount = mutableMapOf<Char, Int>()
        itemList.forEach { item ->
            item.forEach { char ->
                charCount[char] = charCount.getOrDefault(char, 0) + 1
            }
        }

        val sortedCharCount = charCount.entries.sortedByDescending { it.value }.take(3)
        return sortedCharCount.joinToString(separator = "\n") { "${it.key} = ${it.value}" }
    }
}