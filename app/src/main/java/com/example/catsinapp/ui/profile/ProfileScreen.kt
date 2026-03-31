package com.example.catsinapp.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.catsinapp.R
import com.example.catsinapp.ui.theme.GreenDark
import com.example.catsinapp.ui.theme.TextPrimary
import com.example.catsinapp.ui.theme.TextSecondary

private val YellowCard = Color(0xFFF5F7D0)
private val AmberLabel = Color(0xFF8B6914)

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        // ── Аватар питомца ────────────────────────────────
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(YellowCard),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter            = painterResource(R.drawable.cat_buddy_profile),
                contentDescription = "Buddy",
                contentScale       = ContentScale.Fit,
                modifier           = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(12.dp))

        // ── Заголовок ─────────────────────────────────────
        Text(
            text       = "Buddy's Profile",
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary
        )
        Text(
            text     = "Health & Wellness Identity",
            fontSize = 13.sp,
            color    = TextSecondary
        )

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Pet Name ──────────────────────────────────
            ProfileField(label = "PET NAME") {
                Text(
                    text     = "Buddy",
                    fontSize = 16.sp,
                    color    = TextPrimary,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // ── Date of Birth ─────────────────────────────
            ProfileField(label = "DATE OF BIRTH") {
                Row(
                    modifier          = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter            = painterResource(R.drawable.ic_calendar),
                        contentDescription = null,
                        tint               = TextSecondary,
                        modifier           = Modifier.size(18.dp)
                    )
                    Text(
                        text     = "05/12/2021",
                        fontSize = 16.sp,
                        color    = TextPrimary
                    )
                }
            }

            // ── Current Weight ────────────────────────────
            ProfileFieldLabel("CURRENT WEIGHT")
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // KG
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(YellowCard, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text       = "12",
                        fontSize   = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary
                    )
                    Text(
                        text          = "KILOGRAMS (KG)",
                        fontSize      = 10.sp,
                        color         = TextSecondary,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 0.4.sp
                    )
                }
                // G
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(YellowCard, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text       = "450",
                        fontSize   = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary
                    )
                    Text(
                        text          = "GRAMS (G)",
                        fontSize      = 10.sp,
                        color         = TextSecondary,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 0.4.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Change Data button ────────────────────────
            Button(
                onClick  = { navController.navigate("weight_entry") },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(26.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = GreenDark)
            ) {
                Text(
                    text       = "Change Data",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ── Вспомогательные компоненты ────────────────────────────
@Composable
private fun ProfileFieldLabel(label: String) {
    Text(
        text          = label,
        fontSize      = 11.sp,
        color         = AmberLabel,
        fontWeight    = FontWeight.Bold,
        letterSpacing = 0.5.sp
    )
}

@Composable
private fun ProfileField(label: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        ProfileFieldLabel(label)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(YellowCard, RoundedCornerShape(16.dp))
        ) {
            content()
        }
    }
}