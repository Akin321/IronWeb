function deleteAccount(){
	Swal.fire({
	       title: 'Are you sure?',
	       text: "This will delete your account, cart, wallet, coupons, and order history permanently.",
	       icon: 'warning',
	       showCancelButton: true,
	       confirmButtonText: 'Yes, Continue',
	       cancelButtonText: 'Cancel'
	   }).then((result) => {
	       if (result.isConfirmed) {
	           askPasswordAndDelete();
	       }
	   });
}
function askPasswordAndDelete() {
	 Swal.fire({
	        title: 'Enter Your Password',
	        input: 'password',
	        inputLabel: 'Password',
	        inputPlaceholder: 'Enter your password to confirm',
	        inputAttributes: {
	            autocapitalize: 'off',
	            autocorrect: 'off'
	        },
	        showCancelButton: true,
	        confirmButtonText: 'Confirm Delete',
	        cancelButtonText: 'Cancel',
	        showLoaderOnConfirm: true,
			 preConfirm: (password) => {
			        if (!password) {
			            Swal.showValidationMessage('⚠️ Password is required');
			            return false;
			        }

			        return fetch('/user/validate-password', {
			            method: 'POST',
			            headers: {
			                'Content-Type': 'application/json',
			            },
			            body: JSON.stringify({ password })
			        })
			        .then(response => {
			            if (!response.ok) {
			                Swal.showValidationMessage('❌ Incorrect password. Please try again.');
			                throw new Error("Password validation failed");
			            }
			            return response.json();
			        })
			        .then(data => {
			            if (!data.valid) {
			                Swal.showValidationMessage('❌ Incorrect password. Please try again.');
			                throw new Error("Invalid password");
			            }
			        })
			        .catch(error => {
			            // Optional: log or handle unexpected errors
			        });
			    },
			}).then((result) => {
			    if (result.isConfirmed) {
			        // Redirect only if password is validated
			        window.location.href = "/user/delete-account";
			    }
			});
	}
