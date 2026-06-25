package com.example.demofacedetect.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.demofacedetect.domain.Photo

/** Navigation host and screens for a Google Photos-inspired local People experience. */
@Composable fun FacePeopleApp(vm: AppViewModel = hiltViewModel()){
 val nav= rememberNavController(); Scaffold(bottomBar={ NavigationBar { listOf("photos","people","import","settings").forEach{ NavigationBarItem(selected=false,onClick={nav.navigate(it){launchSingleTop=true}},icon={},label={Text(it.replaceFirstChar(Char::uppercase))}) } } }){pad-> NavHost(nav,"photos",Modifier.padding(pad)){ composable("photos"){ PhotoGridScreen("Photos", vm.photos.collectAsState().value) }; composable("people"){ PeopleScreen(vm, onClick={nav.navigate("person/$it")}) }; composable("person/{id}", arguments=listOf(navArgument("id"){type=NavType.LongType})){ val id=it.arguments!!.getLong("id"); val photos by vm.photosForPerson(id).collectAsState(emptyList()); PhotoGridScreen("Person", photos) }; composable("import"){ ImportScreen(vm) }; composable("settings"){ SettingsScreen(vm) } } }
}
@Composable fun PhotoGridScreen(title:String, photos:List<Photo>){ Column(Modifier.fillMaxSize().padding(12.dp)){ Text(title, style=MaterialTheme.typography.headlineMedium); LazyVerticalGrid(GridCells.Adaptive(120.dp), verticalArrangement=Arrangement.spacedBy(4.dp), horizontalArrangement=Arrangement.spacedBy(4.dp)){ items(photos){ AsyncImage(model=it.path, contentDescription=null, modifier=Modifier.aspectRatio(1f), contentScale=ContentScale.Crop) } } } }
@Composable fun PeopleScreen(vm:AppViewModel,onClick:(Long)->Unit){ val people by vm.people.collectAsState(); Column(Modifier.padding(12.dp)){ Text("People & Pets", style=MaterialTheme.typography.headlineMedium); LazyVerticalGrid(GridCells.Adaptive(140.dp), horizontalArrangement=Arrangement.spacedBy(12.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){ items(people){ ElevatedCard(Modifier.clickable{onClick(it.id)}){ AsyncImage(model=it.thumbnailPath, contentDescription=it.name, modifier=Modifier.fillMaxWidth().aspectRatio(1f), contentScale=ContentScale.Crop); Text(it.name, Modifier.padding(horizontal=8.dp)); Text("${it.photoCount} Photos", Modifier.padding(8.dp), style=MaterialTheme.typography.bodySmall) } } } } }
@Composable fun ImportScreen(vm:AppViewModel){ val status by vm.status.collectAsState(); val single=rememberLauncherForActivityResult(PickVisualMedia()){ it?.let{vm.import(listOf(it))} }; val multi=rememberLauncherForActivityResult(PickMultipleVisualMedia(500)){ vm.import(it) }; Column(Modifier.padding(24.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){ Text("Import", style=MaterialTheme.typography.headlineMedium); Button({single.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))}){Text("Gallery")}; Button({multi.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))}){Text("Multi Select Gallery")}; Text(status) } }
@Composable fun SettingsScreen(vm:AppViewModel){ Column(Modifier.padding(24.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){ Text("Settings", style=MaterialTheme.typography.headlineMedium); Text("Similarity threshold: 0.85 (default)"); Text("Offline only: no network permission is requested."); OutlinedButton(onClick=vm::clear){Text("Clear Database")} } }
