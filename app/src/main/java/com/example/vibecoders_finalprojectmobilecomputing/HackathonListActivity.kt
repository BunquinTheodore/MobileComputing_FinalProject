package com.example.vibecoders_finalprojectmobilecomputing

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HackathonListActivity : AppCompatActivity() {
    
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var hackathonAdapter: HackathonAdapter
    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerLocation: Spinner
    
    private val hackathons = mutableListOf<Hackathon>()
    private val allHackathons = mutableListOf<Hackathon>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hackathon_list)
        
        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        
        // Check if user is signed in
        if (auth.currentUser == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        
        setupViews()
        setupSpinners()
        loadHackathons()
    }
    
    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerViewHackathons)
        spinnerMonth = findViewById(R.id.spinnerMonth)
        spinnerLocation = findViewById(R.id.spinnerLocation)
        
        // Setup RecyclerView
        hackathonAdapter = HackathonAdapter(hackathons)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = hackathonAdapter
        
        // Logout button
        findViewById<android.widget.ImageButton>(R.id.btnLogout).setOnClickListener {
            logout()
        }
    }
    
    private fun setupSpinners() {
        // Month spinner
        val months = arrayOf("All Months", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December")
        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = monthAdapter
        
        // Location spinner
        val locations = arrayOf("All Locations", "San Francisco", "New York", "Los Angeles", 
            "Chicago", "Austin", "Seattle", "Boston", "Denver")
        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, locations)
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLocation.adapter = locationAdapter
        
        // Set listeners
        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterHackathons()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        
        spinnerLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterHackathons()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    
    private fun loadHackathons() {
        // Load hackathons from Firestore
        db.collection("hackathons")
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                allHackathons.clear()
                for (document in documents) {
                    val hackathon = document.toObject(Hackathon::class.java)
                    hackathon.id = document.id
                    allHackathons.add(hackathon)
                }
                
                // If no hackathons in Firestore, add sample data
                if (allHackathons.isEmpty()) {
                    addSampleHackathons()
                } else {
                    filterHackathons()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error loading hackathons: ${exception.message}", 
                    Toast.LENGTH_SHORT).show()
                // Add sample data as fallback
                addSampleHackathons()
            }
    }
    
    private fun addSampleHackathons() {
        val sampleHackathons = listOf(
            Hackathon(
                title = "TechCrunch Disrupt Hackathon",
                date = "March 15-17, 2024",
                location = "San Francisco",
                description = "Build the next big thing in 48 hours! Join hundreds of developers, designers, and entrepreneurs."
            ),
            Hackathon(
                title = "AI for Good Hackathon",
                date = "April 22-24, 2024",
                location = "New York",
                description = "Use artificial intelligence to solve real-world problems and make a positive impact."
            ),
            Hackathon(
                title = "Blockchain Innovation Challenge",
                date = "May 10-12, 2024",
                location = "Austin",
                description = "Explore the future of decentralized technology and build innovative blockchain solutions."
            ),
            Hackathon(
                title = "Mobile App Development Sprint",
                date = "June 5-7, 2024",
                location = "Seattle",
                description = "Create amazing mobile applications that will change how people interact with technology."
            ),
            Hackathon(
                title = "Green Tech Hackathon",
                date = "July 18-20, 2024",
                location = "Los Angeles",
                description = "Develop sustainable technology solutions to combat climate change and environmental challenges."
            )
        )
        
        allHackathons.addAll(sampleHackathons)
        filterHackathons()
    }
    
    private fun filterHackathons() {
        val selectedMonth = spinnerMonth.selectedItem.toString()
        val selectedLocation = spinnerLocation.selectedItem.toString()
        
        hackathons.clear()
        
        for (hackathon in allHackathons) {
            var includeHackathon = true
            
            // Filter by month
            if (selectedMonth != "All Months") {
                if (!hackathon.date.contains(selectedMonth, ignoreCase = true)) {
                    includeHackathon = false
                }
            }
            
            // Filter by location
            if (selectedLocation != "All Locations") {
                if (!hackathon.location.contains(selectedLocation, ignoreCase = true)) {
                    includeHackathon = false
                }
            }
            
            if (includeHackathon) {
                hackathons.add(hackathon)
            }
        }
        
        hackathonAdapter.notifyDataSetChanged()
    }
    
    private fun logout() {
        auth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }
}
