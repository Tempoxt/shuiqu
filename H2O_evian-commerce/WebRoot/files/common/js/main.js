jQuery(function($) {
        $(document).ready( function() {
          $('.navbar-wrapper').stickUp({
                        parts: {
                          0:'home',
                          1:'features',
                          2: 'updates',
                          3: 'installation',
                          4: 'one-pager',
                          5: 'extras',
                          6: 'wordpress',
                          7: 'contact'
                        },
                        itemClass: 'menuItem',
                        itemHover: 'active',
                        topMargin: 'auto'
                      });
        });
      });