package com.abdurraahm.spellcorrect.data.local.source

import com.abdurraahm.spellcorrect.data.local.model.WordEntry

object DummyWordEntrySource {
    fun singleWord(): WordEntry =
        WordEntry(
            word = "accurate",
            definition = listOf(
                "free from mistakes or errors",
                "able to produce results that are correct : not making mistakes"
            ),
            type = "adjective",
            ipa = "\u02c8\u00e6kj\u0259r\u0259t"
        )

}


