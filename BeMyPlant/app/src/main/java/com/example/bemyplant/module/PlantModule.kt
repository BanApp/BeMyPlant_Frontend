package com.example.bemyplant.module

import com.example.bemyplant.model.PlantModel
import io.realm.annotations.RealmModule

@RealmModule(classes = [PlantModel::class])
class PlantModule {
}