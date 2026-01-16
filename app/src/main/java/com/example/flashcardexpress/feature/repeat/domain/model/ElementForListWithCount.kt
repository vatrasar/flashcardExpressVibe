package com.example.flashcardexpress.feature.repeat.domain.model

import com.example.flashcardexpress.common.ui.model.ElementForListWithTitle

class ElementForListWithCount(val count:Int,val categoryName:String,val categoryId:Int) : ElementForListWithTitle(categoryName,categoryId){

}