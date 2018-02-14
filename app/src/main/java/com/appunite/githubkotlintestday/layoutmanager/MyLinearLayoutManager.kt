package com.appunite.githubkotlintestday.layoutmanager

import android.content.Context
import android.support.v7.widget.LinearLayoutManager

class MyLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context) : super(context) {
        recycleChildrenOnDetach = true
    }

    constructor(context: Context, horizontal: Int, b: Boolean) : super(context, horizontal, b) {
        recycleChildrenOnDetach = true
    }
}
