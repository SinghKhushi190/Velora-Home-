package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.catalog.VeloraCatalog
import com.example.data.remote.VeloraFirestoreRepository
import com.example.ui.VeloraViewModel
import com.example.ui.auth.VeloraAuthScreen
import com.example.ui.auth.authStateFlow
import com.example.ui.auth.signOutVelora
import com.example.ui.components.ConsultationModalSheet
import com.example.ui.screens.DesignIdeaDetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IdeasScreen
import com.example.ui.screens.LightingDetailScreen
import com.example.ui.screens.LightingScreen
import com.example.ui.screens.StudioScreen
import com.example.ui.theme.VeloraGold
import com.example.ui.theme.VeloraGoldDark
import com.example.ui.theme.VeloraGoldLight
import com.example.ui.theme.VeloraHomeTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

object VeloraRoutes {
    const val HOME = "home"
    const val IDEAS = "ideas"
    const val LIGHTING = "lighting"
    const val STUDIO = "studio"
    const val IDEA_DETAIL = "idea_detail/{ideaId}"
    const val LIGHTING_DETAIL = "lighting_detail/{conceptId}"

    fun ideaDetail(ideaId: String) = "idea_detail/$ideaId"
    fun lightingDetail(conceptId: String) = "lighting_detail/$conceptId"
}

private data class NavDestinationItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val topLevelDestinations = listOf(
    NavDestinationItem(
        route = VeloraRoutes.HOME,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    NavDestinationItem(
        route = VeloraRoutes.IDEAS,
        label = "Ideas",
        selectedIcon = Icons.Filled.GridView,
        unselectedIcon = Icons.Outlined.GridView
    ),
    NavDestinationItem(
        route = VeloraRoutes.LIGHTING,
        label = "Lighting",
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome
    ),
    NavDestinationItem(
        route = VeloraRoutes.STUDIO,
        label = "Studio",
        selectedIcon = Icons.Filled.Bookmark,
        unselectedIcon = Icons.Outlined.BookmarkBorder
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeloraHomeTheme {
                VeloraRootApp()
            }
        }
    }
}

@Composable
fun VeloraRootApp(auth: FirebaseAuth = Firebase.auth) {
    val currentUser by auth.authStateFlow().collectAsStateWithLifecycle(initialValue = auth.currentUser)
    val user = currentUser

    if (user == null) {
        VeloraAuthScreen(onAuthSuccess = {})
    } else {
        val uid = user.uid
        val email = user.email.orEmpty()
        val emailVerified = user.isEmailVerified
        key(uid) {
            AuthenticatedVeloraApp(
                currentUserId = uid,
                currentUserEmail = email,
                isEmailVerified = emailVerified
            )
        }
    }
}

@Composable
fun AuthenticatedVeloraApp(
    currentUserId: String,
    currentUserEmail: String,
    isEmailVerified: Boolean
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { CredentialManager.create(context) }

    val viewModel: VeloraViewModel = viewModel(
        key = currentUserId,
        factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY]) {
                    "APPLICATION_KEY missing from CreationExtras"
                }
                val databaseId = app.getString(R.string.firestore_database_id)
                val db = FirebaseFirestore.getInstance(databaseId)
                VeloraViewModel(
                    repository = VeloraFirestoreRepository(db),
                    currentUserId = currentUserId,
                    currentUserEmail = currentUserEmail,
                    isEmailVerified = isEmailVerified
                )
            }
        }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: VeloraRoutes.HOME
    val isTopLevel = topLevelDestinations.any { it.route == currentRoute }

    fun navigateTopLevel(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 600.dp

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (isTopLevel && !useRail) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        windowInsets = WindowInsets.navigationBars,
                        tonalElevation = 4.dp
                    ) {
                        topLevelDestinations.forEach { dest ->
                            val selected = currentRoute == dest.route
                            NavigationBarItem(
                                selected = selected,
                                onClick = { navigateTopLevel(dest.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                        contentDescription = dest.label
                                    )
                                },
                                label = { Text(dest.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = VeloraGoldDark,
                                    selectedTextColor = VeloraGoldDark,
                                    indicatorColor = VeloraGoldLight
                                ),
                                modifier = Modifier.testTag("nav_tab_${dest.route}")
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (isTopLevel && currentRoute != VeloraRoutes.STUDIO) {
                    ExtendedFloatingActionButton(
                        onClick = { viewModel.openQuoteSheet() },
                        containerColor = VeloraGold,
                        contentColor = Color.White,
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Call,
                                contentDescription = "Book consultation"
                            )
                        },
                        text = { Text("Get a Quote") },
                        modifier = Modifier.testTag("fab_get_quote")
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isTopLevel && useRail) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        topLevelDestinations.forEach { dest ->
                            val selected = currentRoute == dest.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateTopLevel(dest.route) },
                                icon = {
                                    Icon(
                                        imageVector = if (selected) dest.selectedIcon else dest.unselectedIcon,
                                        contentDescription = dest.label
                                    )
                                },
                                label = { Text(dest.label) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = VeloraGoldDark,
                                    selectedTextColor = VeloraGoldDark,
                                    indicatorColor = VeloraGoldLight
                                )
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = VeloraRoutes.HOME,
                    modifier = Modifier.weight(1f)
                ) {
                    composable(VeloraRoutes.HOME) {
                        HomeScreen(
                            onExploreIdeas = { category ->
                                viewModel.selectCategory(category)
                                navigateTopLevel(VeloraRoutes.IDEAS)
                            },
                            onExploreLighting = {
                                navigateTopLevel(VeloraRoutes.LIGHTING)
                            },
                            onOpenQuote = { ref ->
                                viewModel.openQuoteSheet(designReference = ref)
                            },
                            onIdeaClick = { ideaId ->
                                navController.navigate(VeloraRoutes.ideaDetail(ideaId))
                            }
                        )
                    }

                    composable(VeloraRoutes.IDEAS) {
                        IdeasScreen(
                            selectedCategory = uiState.selectedCategory,
                            searchQuery = uiState.searchQuery,
                            ideas = uiState.filteredIdeas,
                            savedIds = uiState.savedIds,
                            onSelectCategory = viewModel::selectCategory,
                            onSearchQueryChange = viewModel::updateSearchQuery,
                            onToggleSave = viewModel::toggleSaved,
                            onIdeaClick = { ideaId ->
                                navController.navigate(VeloraRoutes.ideaDetail(ideaId))
                            },
                            onGetQuoteForIdea = { idea ->
                                viewModel.openQuoteSheet(
                                    designReference = idea.title,
                                    serviceScope = idea.category.label
                                )
                            }
                        )
                    }

                    composable(VeloraRoutes.LIGHTING) {
                        LightingScreen(
                            selectedSeries = uiState.selectedLightingSeries,
                            concepts = uiState.filteredLighting,
                            savedIds = uiState.savedIds,
                            onSelectSeries = viewModel::selectLightingSeries,
                            onToggleSave = viewModel::toggleSaved,
                            onConceptClick = { conceptId ->
                                navController.navigate(VeloraRoutes.lightingDetail(conceptId))
                            }
                        )
                    }

                    composable(VeloraRoutes.STUDIO) {
                        StudioScreen(
                            uiState = uiState,
                            onSignOut = {
                                signOutVelora(
                                    credentialManager = credentialManager,
                                    scope = scope
                                )
                            },
                            onDismissCloudError = viewModel::dismissCloudError,
                            onSelectHomeSize = viewModel::setEstimatorHomeSize,
                            onSelectScope = viewModel::setEstimatorScope,
                            onSelectTier = viewModel::setEstimatorTier,
                            onApplyEstimateToQuote = { summary ->
                                viewModel.openQuoteSheet(
                                    designReference = summary,
                                    serviceScope = "Full Home & Lighting"
                                )
                            },
                            onToggleSave = viewModel::toggleSaved,
                            onIdeaClick = { ideaId ->
                                navController.navigate(VeloraRoutes.ideaDetail(ideaId))
                            },
                            onLightingClick = { conceptId ->
                                navController.navigate(VeloraRoutes.lightingDetail(conceptId))
                            },
                            onNameChange = viewModel::updateFormName,
                            onPhoneChange = viewModel::updateFormPhone,
                            onCityChange = viewModel::updateFormCity,
                            onPropertyTypeChange = viewModel::updateFormPropertyType,
                            onScopeChange = viewModel::updateFormServiceScope,
                            onNotesChange = viewModel::updateFormNotes,
                            onSubmitConsultation = viewModel::submitConsultation,
                            onDismissSuccess = viewModel::dismissSuccessBanner,
                            onUpdateLeadStatus = viewModel::updateLeadStatus,
                            onDeleteConsultation = viewModel::deleteConsultation
                        )
                    }

                    composable(
                        route = VeloraRoutes.IDEA_DETAIL,
                        arguments = listOf(navArgument("ideaId") { type = NavType.StringType })
                    ) { entry ->
                        val ideaId = entry.arguments?.getString("ideaId").orEmpty()
                        val idea = VeloraCatalog.designIdeas.find { it.id == ideaId }
                            ?: VeloraCatalog.designIdeas.first()
                        DesignIdeaDetailScreen(
                            idea = idea,
                            isSaved = uiState.savedIds.contains(idea.id),
                            onBack = { navController.popBackStack() },
                            onToggleSave = { viewModel.toggleSaved(idea.id) },
                            onBookQuote = {
                                viewModel.openQuoteSheet(
                                    designReference = idea.title,
                                    serviceScope = idea.category.label
                                )
                            }
                        )
                    }

                    composable(
                        route = VeloraRoutes.LIGHTING_DETAIL,
                        arguments = listOf(navArgument("conceptId") { type = NavType.StringType })
                    ) { entry ->
                        val conceptId = entry.arguments?.getString("conceptId").orEmpty()
                        val concept = VeloraCatalog.lightingConcepts.find { it.id == conceptId }
                            ?: VeloraCatalog.lightingConcepts.first()
                        LightingDetailScreen(
                            concept = concept,
                            isSaved = uiState.savedIds.contains(concept.id),
                            onBack = { navController.popBackStack() },
                            onToggleSave = { viewModel.toggleSaved(concept.id) },
                            onBookLightingQuote = {
                                viewModel.openQuoteSheet(
                                    designReference = "${concept.series.label}: ${concept.title}",
                                    serviceScope = "Architectural Lighting"
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (uiState.isQuoteSheetOpen) {
        ConsultationModalSheet(
            formState = uiState.formState,
            onDismiss = viewModel::closeQuoteSheet,
            onNameChange = viewModel::updateFormName,
            onPhoneChange = viewModel::updateFormPhone,
            onCityChange = viewModel::updateFormCity,
            onPropertyTypeChange = viewModel::updateFormPropertyType,
            onScopeChange = viewModel::updateFormServiceScope,
            onNotesChange = viewModel::updateFormNotes,
            onSubmit = viewModel::submitConsultation,
            onDismissSuccess = viewModel::dismissSuccessBanner
        )
    }
}
