package com.example.onlineshop.Activity

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.rememberAsyncImagePainter
import com.example.onlineshop.Model.ItemsModel
import com.example.onlineshop.R
import com.example.onlineshop.R.color.lightBrown
import com.example.project1762.Helper.ChangeNumberItemsListener
import com.example.project1762.Helper.ManagmentCart

class CartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartScreen  (ManagmentCart(this) ,
            onBackClick= {
                finish()
            })
        }
    }
}

fun calculatorCart(managmentCart: ManagmentCart, tax: MutableState<Double>){
    val percentTax=0.02
    tax.value= Math.round((managmentCart.getTotalFee()*percentTax)*100)/100.0
}

@Composable
private fun CartScreen(
    managmentCart: ManagmentCart= ManagmentCart(LocalContext.current),
    onBackClick:()->Unit
){
    val cartItems= remember { mutableStateOf(managmentCart.getListCart()) }
    val tax = remember { mutableStateOf(0.0) }
    calculatorCart(managmentCart, tax)
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp))
    {
        ConstraintLayout(modifier = Modifier.padding(top = 36.dp)) {
            val (backBtn, cartTxt) = createRefs()
            Text(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(cartTxt){centerTo(parent)}
                , text = "Your Cart",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
            )
            Image(painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable{ onBackClick() }
                    .constrainAs(backBtn){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)

                    }
                )
        }
        if(cartItems.value.isEmpty()){
            Text(text = "Cart Is Empty", modifier = Modifier.align(Alignment.CenterHorizontally))
        }else{
            CartList(cartItems = cartItems.value, managmentCart){
                cartItems.value = ArrayList(managmentCart.getListCart().map { it.copy() })
                calculatorCart(managmentCart, tax)
            }
            CartSummary(
                itemTotal = managmentCart.getTotalFee(),
                tax = tax.value,
                delivery = 10.0
            )
        }
    }
}

@Composable
fun CartSummary(itemTotal: Double, tax: Double, delivery: Double) {
    val total = itemTotal + tax + delivery
    Column (
        modifier =  Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Item Total:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )
            Text(text = "$$itemTotal")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Tax:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )
            Text(text = "$$tax")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "Delivery:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )
            Text(text = "$$delivery")
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Total:",
                    Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.darkBrown)
                )
                Text(text = "$$total")
            }
            Button(
                onClick = {},
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.darkBrown)
                ),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ){
                Text(
                    text="Check Out",
                    fontSize = 18.sp,
                    color=Color.White
                )
            }
        }
    }
}

@Composable
fun CartList(
    cartItems: ArrayList<ItemsModel>,
    managmentCart: ManagmentCart,
    onItemChange: () -> Unit) {
    LazyColumn(Modifier.padding(top=16.dp)){
        items(cartItems.size) { index ->
            CartItem(
                cartItems,
                index=index,
                managmentCart=managmentCart,
                onItemChange=onItemChange
            )
        }
    }
}

@Composable
fun CartItem(
    cartItems: ArrayList<ItemsModel>,
    index: Int,
    managmentCart: ManagmentCart,
    onItemChange: () -> Unit
) {
    val currentItem = cartItems[index]
    
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        val (pic, titleText, feeEachTime, totalEachItem, Quantity) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(currentItem.picUrl[0]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .background(
                    colorResource(R.color.lightBrown),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(8.dp)
                .constrainAs(pic){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        Text(text = currentItem.title,
            modifier = Modifier
                .constrainAs(titleText){
                    start.linkTo(pic.end)
                    top.linkTo(pic.top)
                }
                .padding(start = 8.dp, top = 8.dp)
            )
        Text(text = "$${currentItem.price}", color = colorResource(R.color.darkBrown), modifier = Modifier
            .constrainAs(feeEachTime){
                start.linkTo(titleText.start)
                top.linkTo(titleText.bottom)
            }
            .padding(start = 8.dp, top = 8.dp)
        )
        Text(text = "$${currentItem.numberInCart*currentItem.price}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
            .constrainAs(totalEachItem){
                start.linkTo(titleText.start)
                bottom.linkTo(pic.bottom)
            }
            .padding(start = 8.dp, top = 8.dp)
        )
        ConstraintLayout(
            modifier=Modifier
                .width(100.dp)
                .constrainAs(Quantity){
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .background(
                    colorResource(R.color.lightBrown),
                    shape = RoundedCornerShape(100.dp)
                )
        ){
            val (plusCartBtn, minusCartBtn, numberItemText) = createRefs()
            Text(text = currentItem.numberInCart.toString(), color = Color.Black, fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(numberItemText){
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                )
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        colorResource(R.color.darkBrown),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .constrainAs(plusCartBtn){
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managmentCart.plusItem(
                            cartItems,
                            index,
                            object: ChangeNumberItemsListener{
                                override fun onChanged() {
                                    onItemChange()
                                }
                            }
                        )
                    }

            ){
                Text(
                    text="+",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        colorResource(R.color.white),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .constrainAs(minusCartBtn){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managmentCart.minusItem(cartItems,
                            index, object: ChangeNumberItemsListener{
                                override fun onChanged() {
                                    onItemChange()
                                }
                            })
                    }
            ){
                Text(
                    text = "-",
                    color = colorResource(R.color.darkBrown),
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
