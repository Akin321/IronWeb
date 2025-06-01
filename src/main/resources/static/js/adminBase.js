
    window.onpageshow = function(event) {
        if (event.persisted || performance.navigation.type === 2) {
            // Reload the page so session check re-runs
            window.location.reload();
        }
    };

