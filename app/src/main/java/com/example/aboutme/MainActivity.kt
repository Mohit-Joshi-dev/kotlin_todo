package com.example.aboutme

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), ITodoRVAdapter {

    lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this);
        val adapter = TodoRVAdapter(this, this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(TodoViewModel::class.java)
        viewModel.allTodo.observe(this, Observer {list ->
            list?.let {
                adapter.updateList(it)
            }
        })

    }

    override fun onItemClicked(todo: Todo) {
        viewModel.deleteTodo(todo)
        Toast.makeText(this, "${todo.text} Deleted", Toast.LENGTH_SHORT).show()
    }

    fun addTodo(view: View) {
        val input = findViewById<EditText>(R.id.input)
        val todoText = input.text.toString()
        if(todoText.isNotEmpty()) {
            viewModel.insertTodo(Todo(todoText))
            input.text.clear()
            input.clearFocus()
            hideKeyboard(view)
            Toast.makeText(this, "$todoText Added", Toast.LENGTH_SHORT).show()
        }
    }
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}