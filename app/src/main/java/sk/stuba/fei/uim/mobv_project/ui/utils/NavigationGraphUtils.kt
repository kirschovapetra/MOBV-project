package sk.stuba.fei.uim.mobv_project.ui.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import sk.stuba.fei.uim.mobv_project.R

object NavigationGraphUtils {
    fun changeNavGraphStartDestination(fragment: Fragment, newStartDestinationId: Int) {
        changeNavGraphStartDestination(fragment, newStartDestinationId, R.navigation.nav_graph)
    }

    fun changeNavGraphStartDestination(fragment: Fragment, newStartDestinationId: Int, newNavigationGraphId: Int) {
        val navController = fragment.findNavController()
        val newGraph = navController.navInflater.inflate(newNavigationGraphId)

        newGraph.startDestination = newStartDestinationId
        navController.graph = newGraph
    }
}