package com.example.university_app

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class InfoFragment : Fragment() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArraylist : MutableList<FacultiesModel>
    private lateinit var tempArraylist : MutableList<FacultiesModel>
    lateinit var imageid : MutableList<Int>
    lateinit var heading : MutableList<String>
    @SuppressLint("MissingInflatedId", "CutPasteId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root_view = inflater.inflate(R.layout.fragment_info, container, false)
        var main_view: View? = root_view
        var databaseAccess: DatabaseAccessFacult = DatabaseAccessFacult.getInstance(requireContext())
        databaseAccess.open()
        var dataList: MutableList<FacultiesModel> = databaseAccess.getAllData()

        databaseAccess.close()
        root_view.findViewById<TextView>(R.id.info_site).setOnClickListener {
            val url = "https://lnu.edu.ua/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        root_view.findViewById<ImageButton>(R.id.map_button).setOnClickListener {
            val url = "https://www.google.com/maps/place/%D0%9B%D1%8C%D0%B2%D1%96%D0%B2%D1%81%D1%8C%D0%BA%D0%B8%D0%B9+%D0%BD%D0%B0%D1%86%D1%96%D0%BE%D0%BD%D0%B0%D0%BB%D1%8C%D0%BD%D0%B8%D0%B9+%D1%83%D0%BD%D1%96%D0%B2%D0%B5%D1%80%D1%81%D0%B8%D1%82%D0%B5%D1%82+%D1%96%D0%BC%D0%B5%D0%BD%D1%96+%D0%86%D0%B2%D0%B0%D0%BD%D0%B0+%D0%A4%D1%80%D0%B0%D0%BD%D0%BA%D0%B0/@49.840348,24.020108,17z/data=!3m1!4b1!4m6!3m5!1s0x473add717532cff9:0x1ea627f45b408179!8m2!3d49.840348!4d24.0222967!16zL20vMDZfc2dr"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        val btn_contacts = root_view.findViewById<Button>(R.id.contact_button)
        btn_contacts.setOnClickListener {
            val dialogBining = layoutInflater.inflate(R.layout.contacts_dialog, null)
            val context = requireActivity()
            val contact_dialog = Dialog(context)
            contact_dialog.setContentView(dialogBining)
            var dialog: Dialog? = contact_dialog
            contact_dialog.setCancelable(true)
            contact_dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            imageid = mutableListOf<Int>()
            heading = mutableListOf<String>()
            for(i in dataList){
                imageid.add(i.logoID)
            }
            for(i in dataList){
                heading.add(i.name)
            }
//            Log.d("biolog_logo","${R.drawable.biolog_map}")
//            Log.d("biolog_logo","${R.drawable.geography_map}")
//            Log.d("biolog_logo","${R.drawable.geology_map}")
//            Log.d("biolog_logo","${R.drawable.econom_map}")
//            Log.d("biolog_logo","${R.drawable.electronic_map}")
//            Log.d("biolog_logo","${R.drawable.journ_map}")
//            Log.d("biolog_logo","${R.drawable.lingua_map}")
//            Log.d("biolog_logo","${R.drawable.history_map}")
//            Log.d("biolog_logo","${R.drawable.kultart_map}")
//            Log.d("biolog_logo","${R.drawable.main_map}")
//            Log.d("biolog_logo","${R.drawable.intrel_map}")
//            Log.d("biolog_logo","${R.drawable.pedagogy_map}")
//            Log.d("biolog_logo","${R.drawable.ami_map}")
//            Log.d("biolog_logo","${R.drawable.financial_map}")
//            Log.d("biolog_logo","${R.drawable.physics_map}")
//            Log.d("biolog_logo","${R.drawable.philology_map}")
//            Log.d("biolog_logo","${R.drawable.main_map}")
//            Log.d("biolog_logo","${R.drawable.chem_map}")
//            Log.d("biolog_logo","${R.drawable.law_map}")



            val input = dialogBining.findViewById<TextInputEditText>(R.id.input_2_end)
//            Log.d("type","${input::class.simpleName}")
            input.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    input.setText("")
                }
//                else{
//                    input.setText("Назва деканату")
//                }
            }

            newRecyclerView = dialogBining.findViewById(R.id.recycler_facult)
            newRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            newRecyclerView.setHasFixedSize(true)

            newArraylist = mutableListOf<FacultiesModel>()

            getData(input,dataList,dialog,main_view)

            contact_dialog.show()

            val cancel_btn = contact_dialog.findViewById<ImageButton>(R.id.button_close)
            cancel_btn.setOnClickListener {
                contact_dialog.dismiss()
                root_view.findViewById<ImageView>(R.id.info_logo).setImageResource(R.drawable.logofaculty)
                root_view.findViewById<TextView>(R.id.info_name).setText("© Львівський національний університет імені Івана Франка")
                root_view.findViewById<TextView>(R.id.info_location).setText("Адреса: вул. Університетська 1, м. Львів, 79000")
                root_view.findViewById<TextView>(R.id.info_phone).setText("Телефон: (+38 032) 260-34-02")
                root_view.findViewById<TextView>(R.id.info_email).setText("Пошта: zag_kan@lnu.edu.ua")
                root_view.findViewById<TextView>(R.id.info_site).setText("Сайт: lnu.edu.ua")
                root_view.findViewById<ImageButton>(R.id.map_button).setBackgroundResource(R.drawable.main_map)
                root_view.findViewById<TextView>(R.id.info_site).setOnClickListener {
                    val url = "https://lnu.edu.ua/"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
                root_view.findViewById<ImageButton>(R.id.map_button).setOnClickListener {
                    val url = "https://www.google.com/maps/place/%D0%9B%D1%8C%D0%B2%D1%96%D0%B2%D1%81%D1%8C%D0%BA%D0%B8%D0%B9+%D0%BD%D0%B0%D1%86%D1%96%D0%BE%D0%BD%D0%B0%D0%BB%D1%8C%D0%BD%D0%B8%D0%B9+%D1%83%D0%BD%D1%96%D0%B2%D0%B5%D1%80%D1%81%D0%B8%D1%82%D0%B5%D1%82+%D1%96%D0%BC%D0%B5%D0%BD%D1%96+%D0%86%D0%B2%D0%B0%D0%BD%D0%B0+%D0%A4%D1%80%D0%B0%D0%BD%D0%BA%D0%B0/@49.840348,24.020108,17z/data=!3m1!4b1!4m6!3m5!1s0x473add717532cff9:0x1ea627f45b408179!8m2!3d49.840348!4d24.0222967!16zL20vMDZfc2dr"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
        }
        return root_view
    }
    private fun getData(input:TextInputEditText,dataList: MutableList<FacultiesModel>,dialog:Dialog?,main_view: View?){
        input.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                tempArraylist = mutableListOf<FacultiesModel>()
//                    if(p0.toString() == "Назва деканату"){
//                        tempArraylist.addAll(newArraylist)
//                    }
                for(i in dataList){
                    if(i.name.lowercase().contains(p0.toString().lowercase())){
                        tempArraylist.add(i)
                    }
                }
                if(tempArraylist.isEmpty()){
                    tempArraylist.add(FacultiesModel(logoID=R.drawable.sad,name="Збігів не знайдено"))
                }
                newRecyclerView.adapter = Adapter_facult(tempArraylist,dialog,main_view)
            }

        })


        newRecyclerView.adapter = Adapter_facult(dataList,dialog,main_view)
    }

}


