package com.example.vibecoders_finalprojectmobilecomputing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HackathonAdapter(private val hackathons: List<Hackathon>) : 
    RecyclerView.Adapter<HackathonAdapter.HackathonViewHolder>() {

    class HackathonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvHackathonTitle)
        val dateTextView: TextView = itemView.findViewById(R.id.tvHackathonDate)
        val locationTextView: TextView = itemView.findViewById(R.id.tvHackathonLocation)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvHackathonDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HackathonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hackathon, parent, false)
        return HackathonViewHolder(view)
    }

    override fun onBindViewHolder(holder: HackathonViewHolder, position: Int) {
        val hackathon = hackathons[position]
        holder.titleTextView.text = hackathon.title
        holder.dateTextView.text = "Date: ${hackathon.date}"
        holder.locationTextView.text = "Location: ${hackathon.location}"
        holder.descriptionTextView.text = hackathon.description
    }

    override fun getItemCount(): Int = hackathons.size
}
