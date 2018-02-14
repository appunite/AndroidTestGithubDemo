package com.appunite.githubkotlintestday.view.main

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.appunite.githubkotlintestday.R
import com.jacekmarchwicki.universaladapter.ViewHolderManager

class RepositoryViewHolder(itemView: View) : ViewHolderManager.BaseViewHolder<RepositoryAdapterItem>(itemView) {

    override fun bind(item: RepositoryAdapterItem) {
        itemView.findViewById<TextView>(R.id.repo_name).text = item.repository.name
        itemView.findViewById<TextView>(R.id.repo_description).text = item.repository.description

        itemView.findViewById<View>(R.id.repo_body).setOnClickListener {
            if (item.repository.openIssues > 0) {
                item.itemClickObserver.onNext(Any())
            } else {
                Toast.makeText(itemView.context, "No open issues for this repository", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
