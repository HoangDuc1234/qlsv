package prj.hoangduc1234first

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import android.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import prj.hoangduc1234first.R

class MainActivity : Activity() {
    private lateinit var studentAdapter: StudentAdapter
    private val studentList = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        setupRecyclerView(recyclerView)
        btnAdd.setOnClickListener { showAddStudentDialog() }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        studentAdapter = StudentAdapter { position -> showDeleteDialog(position) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = studentAdapter
        recyclerView.itemAnimator = CustomItemAnimator()

        // Submit initial list
        studentAdapter.submitList(studentList.toList())

        // Setup swipe to delete
        setupSwipeToDelete(recyclerView)
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedStudent = studentAdapter.currentList[position]

                // Create a new list without the swiped item
                val newList = studentAdapter.currentList.toMutableList()
                newList.removeAt(position)

                // Update the adapter
                studentAdapter.submitList(newList)

                // Show toast notification
                Toast.makeText(
                    this@MainActivity,
                    "Đã xóa ${deletedStudent.name}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }

    private fun showAddStudentDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etMSSV = dialogView.findViewById<EditText>(R.id.etMSSV)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Thêm Sinh Viên")
        builder.setView(dialogView)
        builder.setPositiveButton("Thêm") { _, _ ->
                val name = etName.text.toString()
                val mssv = etMSSV.text.toString()
                if (name.isNotEmpty() && mssv.isNotEmpty()) {
                    // Create a new list with the new student at the beginning
                    val newList = studentAdapter.currentList.toMutableList()
                    newList.add(0, Student(name, mssv))
                    studentAdapter.submitList(newList)

                    // Update our local list for future reference
                    studentList.clear()
                    studentList.addAll(newList)
                } else {
                    Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
                }
            }
        builder.setNegativeButton("Hủy", null)
        builder.show()
    }

    private fun showDeleteDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Xóa sinh viên")
        builder.setMessage("Bạn có chắc muốn xóa sinh viên này?")
        builder.setPositiveButton("Xóa") { _, _ ->
                // Create a new list without the deleted student
                val newList = studentAdapter.currentList.toMutableList()
                newList.removeAt(position)
                studentAdapter.submitList(newList)

                // Update our local list for future reference
                studentList.clear()
                studentList.addAll(newList)
            }
        builder.setNegativeButton("Hủy", null)
        builder.show()
    }
}
