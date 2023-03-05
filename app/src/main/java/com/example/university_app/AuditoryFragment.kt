package com.example.university_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class AuditoryFragment : Fragment() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArraylist : ArrayList<Audiences>
    lateinit var imageid : Array<Int>
    lateinit var heading : Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root_view = inflater.inflate(R.layout.fragment_auditory, container, false)
        imageid = arrayOf(R.drawable.lock,R.drawable.lock,R.drawable.lock,R.drawable.lock)
        heading = arrayOf("413","413","413","413")
        newRecyclerView = root_view.findViewById(R.id.recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newRecyclerView.setHasFixedSize(true)

        newArraylist = arrayListOf<Audiences>()
        getData()
        return root_view
    }
    private fun getData(){
        for(i in imageid.indices){
            val rooms = Audiences(imageid[i],heading[i])
            newArraylist.add(rooms)
        }
        newRecyclerView.adapter = Adapter_aud(newArraylist)
    }

}