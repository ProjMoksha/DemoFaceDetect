package com.example.demofacedetect.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demofacedetect.repository.FacePeopleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/** MVVM state holder for Compose. It exposes flows and delegates business work to the repository. */
@HiltViewModel class AppViewModel @Inject constructor(private val repo: FacePeopleRepository): ViewModel(){
 val photos=repo.observePhotos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
 val people=repo.observePeople().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
 private val _status=MutableStateFlow("Ready for offline import"); val status:StateFlow<String> = _status
 fun photosForPerson(id:Long)=repo.observePhotosForPerson(id)
 fun import(uris:List<Uri>)=viewModelScope.launch{ _status.value="Importing ${uris.size} photo(s)…"; _status.value="Queued ${repo.importUris(uris)} new photo(s) for processing" }
 fun clear()=viewModelScope.launch{repo.clearDatabase(); _status.value="Local database cleared"}
}
