package com.abdurraahm.spellcorrect.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.data.local.model.BottomSheetButtonData
import com.abdurraahm.spellcorrect.ui.component.PrimaryButton

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DefaultBottomSheet(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    title: String,
    onBottomSheetDismissRequest: () -> Unit,
    buttonData: List<BottomSheetButtonData>?
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onBottomSheetDismissRequest,
            sheetState = rememberModalBottomSheetState(),
        ) {
            // Sheet content
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge
                )
                buttonData?.forEach { data ->
                    PrimaryButton(onClick = data.onClick, text = data.text)
                }
            }
        }
    }
}