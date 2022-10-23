let a = document.querySelectorAll('.textGreen');

for(let b of a){
    b.addEventListener("mouseover", function(){
        this.style.color = "red";
    });
    b.addEventListener("mouseout", function(){
        this.style.color = 'rgb(0, 189, 18)'
    });
};

let inputs = document.querySelectorAll('.inputPassChange');

for(let elem of inputs){
    elem.addEventListener("focus", function(){
        this.style.backgroundColor = 'rgb(206, 252, 209)';
    });
    elem.addEventListener("blur", function(){
        this.style.backgroundColor = '';
    });
    elem.addEventListener("mouseover", function(){
        this.style.borderColor = 'rgb(0, 189, 18)';
        this.style.boxShadow = '0 0 12px rgb(0, 189, 18)'
    });
    elem.addEventListener("mouseout", function(){
        this.style.borderColor = '';
        this.style.boxShadow = ''
    });
};

let main = document.querySelector('.mainContainer');

main.addEventListener("mouseover", function(){
    this.style.boxShadow = '0 0 12px rgb(0, 189, 18)'
});
main.addEventListener("mouseout", function(){
    this.style.boxShadow = '0 0 12px rgb(190, 190, 190)'
});

let cancell = document.querySelector('.buttonCancell');

cancell.addEventListener("mouseover", function(){
    this.style.boxShadow = '0 0 12px  red'
});
cancell.addEventListener("mouseout", function(){
    this.style.boxShadow = ''
});

function seeInvize(){
    let safePass = document.querySelector('.safePass').value;
    let invize = document.querySelector('.invize');
    if(safePass.length >= 7){
        invize.style.display = 'block';
        setTimeout(function () {
            invize.style.display = 'none';
        }, 4000);
}
}
function redBorder(){
    let validConfirm = document.querySelector('.validConfirm').value;
    let validConfirmForm = document.querySelector('.validConfirm');
    let safePass = document.querySelector('.safePass').value;
    if(safePass != validConfirm){
        validConfirmForm.addEventListener("input", function(){
            validConfirmForm.style.borderColor = 'red';
            validConfirmForm.style.boxShadow = '0 0 12px red'
        });
        validConfirmForm.addEventListener("input", function(){
            validConfirmForm.style.borderColor = 'red';
            validConfirmForm.style.boxShadow = '0 0 12px red'
        });
        validConfirmForm.addEventListener("change", function(){
            validConfirmForm.style.borderColor = 'red';
            validConfirmForm.style.boxShadow = '0 0 12px red'
        });
        validConfirmForm.addEventListener("mouseover", function(){
            validConfirmForm.style.borderColor = 'red';
            validConfirmForm.style.boxShadow = '0 0 12px red'
        });
        validConfirmForm.addEventListener("mouseout", function(){
            validConfirmForm.style.borderColor = 'red';
            validConfirmForm.style.boxShadow = '0 0 12px red'
        });
    }
    if(safePass === validConfirm){
        validConfirmForm.addEventListener("input", function(){
            validConfirmForm.style.borderColor = 'red';
            validConfirmForm.style.boxShadow = '0 0 12px red'
        });
        validConfirmForm.addEventListener("input", function(){
            validConfirmForm.style.borderColor = 'rgb(0, 189, 18)';
            validConfirmForm.style.boxShadow = '0 0 12px rgb(0, 189, 18)'
        });
        validConfirmForm.addEventListener("change", function(){
            validConfirmForm.style.borderColor = 'rgb(0, 189, 18)';
            validConfirmForm.style.boxShadow = '0 0 12px rgb(0, 189, 18)'
        });
        validConfirmForm.addEventListener("mouseover", function(){
            validConfirmForm.style.borderColor = 'rgb(0, 189, 18)';
            validConfirmForm.style.boxShadow = '0 0 12px rgb(0, 189, 18)'
        });
        validConfirmForm.addEventListener("mouseout", function(){
            validConfirmForm.style.borderColor = 'rgb(0, 189, 18)';
            validConfirmForm.style.boxShadow = '0 0 12px rgb(0, 189, 18)'
        });
    }
}

let shadow = document.querySelector('.bodyy');
shadow.addEventListener('mouseout', function(e){
    shadow.style.backgroundColor = 'rgb(204, 203, 203)'
});


let pass = document.querySelector('.oldPassword');
let eye = document.querySelector('.formOldPass');
eye.addEventListener('click', function(){
    if(pass.getAttribute('type') == 'password'){
    pass.setAttribute('type', 'text');
} else {
    pass.setAttribute('type', 'password');
}
});
