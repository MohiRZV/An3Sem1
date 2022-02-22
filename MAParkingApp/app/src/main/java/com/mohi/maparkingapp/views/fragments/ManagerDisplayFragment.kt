package com.mohi.maparkingapp.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mohi.maparkingapp.R
import com.mohi.maparkingapp.domain.Lot
import com.mohi.maparkingapp.views.adapter.AdapterClickListener
import com.mohi.maparkingapp.views.adapter.EntityAdapter

/**
 *   created by Mohi on 1/24/2022
 */
class ManagerDisplayFragment: Fragment() {

    private lateinit var adapter: EntityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()
    }

    private fun initialize() {
        setRecyclerView()
        requireActivity().findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            //navigate to add fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.activity_manager_fragment_container, ManagerRegisterFragment())
                .addToBackStack(ManagerDisplayFragment::class.java.name)
                .commit()
        }
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        val clickListener: AdapterClickListener = object: AdapterClickListener{
            override fun onClick(entity: Lot) {
                //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }

        val rv = requireActivity().findViewById<RecyclerView>(R.id.manager_rv)

        adapter = EntityAdapter(clickListener)
        rv.adapter = adapter
        rv.setHasFixedSize(false)

        rv.layoutManager = layoutManager

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //Toast.makeText(this, "on Move", Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //Remove swiped item from list and notify the RecyclerView
                val position = viewHolder.adapterPosition
                adapter.remove(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rv)
    }
}