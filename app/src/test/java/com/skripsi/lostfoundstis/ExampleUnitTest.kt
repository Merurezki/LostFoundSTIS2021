import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), OnItemSelectedListener {
    var os_spinner: Spinner? = null
    var version_spinner: Spinner? = null
    var os_spin = arrayOf<String?>("Android", "iOS", "Windows")
    var version_android: ArrayList<*> = ArrayList<Any?>()
    var version_ios: ArrayList<*> = ArrayList<Any?>()
    var version_windows: ArrayList<*> = ArrayList<Any?>()
    var adapter2: ArrayAdapter<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        os_spinner = findViewById(R.id.os_spinner)
        version_spinner = findViewById(R.id.version_spinner)
        loadArrayList()
        os_spinner.setOnItemSelectedListener(this)
        version_spinner.setOnItemSelectedListener(this)
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, R.layout.simple_list_item_1, os_spin)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        os_spinner.setAdapter(adapter)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (parent.id == R.id.os_spinner) {
            if (position == 0) {
                adapter2 = ArrayAdapter(
                    this,
                    R.layout.simple_list_item_1, version_android
                )
            } else if (position == 1) {
                adapter2 = ArrayAdapter(
                    this,
                    R.layout.simple_list_item_1, version_ios
                )
            } else if (position == 2) {
                adapter2 = ArrayAdapter(
                    this,
                    R.layout.simple_list_item_1, version_windows
                )
            }
            adapter2!!.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            version_spinner!!.adapter = adapter2
        } else if (parent.id == R.id.version_spinner) {
            Toast.makeText(this, "" + version_spinner!!.selectedItem.toString(), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
    private fun loadArrayList() {
        version_android.add("Cupcake")
        version_android.add("Donut")
        version_android.add("Eclair")
        version_android.add("Froyo")
        version_android.add("Gingerbread")
        version_android.add("Honeycomb")
        version_android.add("Ice Cream Sandwich")
        version_android.add("Jelly Bean")
        version_android.add("KitKat")
        version_android.add("Lollipop")
        version_android.add("Marshmallow")
        version_android.add("Nougat")
        version_android.add("Oreo")
        version_android.add("Pie")
        version_ios.add("iOS 4")
        version_ios.add("iOS 5")
        version_ios.add("iOS 6")
        version_ios.add("iOS 7")
        version_ios.add("iOS 8")
        version_ios.add("iOS 9")
        version_ios.add("iOS 10")
        version_ios.add("iOS 11")
        version_ios.add("iOS 12")
        version_ios.add("iOS 13")
        version_windows.add("Windows 1.0")
        version_windows.add("Windows 2.0")
        version_windows.add("Windows 3.0")
        version_windows.add("Windows 95")
        version_windows.add("Windows NT 4.0")
        version_windows.add("Windows 98")
        version_windows.add("Windows 2000")
        version_windows.add("Windows Me")
        version_windows.add("Windows XP")
        version_windows.add("Windows Vista")
        version_windows.add("Windows 7")
        version_windows.add("Windows 8")
        version_windows.add("Windows 10")
    }
}