<%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/16/2017
  Time: 2:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    div.tab {
        overflow: hidden;
        border: 1px solid #ccc;
        background-color: #7fb0cc;
    }

    /* Style the links inside the tab */
    div.tab a {
        display: block;
        text-align: center;
        padding: 14px 16px;
        text-decoration: none;
        transition: 0.3s;
        font-size: 17px;
        border-right:1px solid #6998cc;
        display: inline-block;
        font-weight:bold;
        color:white;
    }

    /* Change background color of links on hover */
    div.tab a:hover {
        background-color: #2c71c9;
    }

    /* Create an active/current tablink class */
    div.tab a:focus, .active {
        background-color: #2c71c9;
    }

    .active{
        background-color: #2c71c9;
    }

    /* Style the tab content */
    .tabcontent {
        display: none;
        padding: 6px 12px;
        border: 1px solid #ccc;
        border-top: none;
        background:white;
    }
</style>

<script>
    function openTab(evt, tabName) {
        // Declare all variables
        var i, tabcontent, tablinks;

        // Get all elements with class="tabcontent" and hide them
        tabcontent = document.getElementsByClassName("tabcontent");
        for (i = 0; i < tabcontent.length; i++) {
            tabcontent[i].style.display = "none";
        }

        // Get all elements with class="tablinks" and remove the class "active"
        tablinks = document.getElementsByClassName("tablinks");
        for (i = 0; i < tablinks.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" active", "");
            tablinks[i].style.backgroundColor="#7fb0cc";
        }

        // Show the current tab, and add an "active" class to the link that opened the tab
        document.getElementById(tabName).style.display = "block";
        document.getElementsByName(tabName)[0].style.backgroundColor="#2c71c9";
        //evt.currentTarget.className += " active";
        document.cookie='currentTab='+tabName;

    }


    function getCookie(name)
    {
        var start = document.cookie.indexOf( name + "=" );
        var len = start + name.length + 1;
        if ( ( !start ) && ( name != document.cookie.substring( 0, name.length ) ) ) {
            return null;
        }
        if ( start == -1 ) return null;
        var end = document.cookie.indexOf( ";", len );
        if ( end == -1 ) end = document.cookie.length;
        return unescape( document.cookie.substring( len, end ) );
    }

    function doload()
    {
        var scrollTop = getCookie ("scrollTop");
        var scrollLeft = getCookie("scrollLeft");
        var currentTab=getCookie("currentTab");

        if (!isNaN(scrollTop))
        {
            document.body.scrollTop = scrollTop;
            document.documentElement.scrollTop = scrollTop;

        }
        if (!isNaN(scrollLeft))
        {
            document.body.scrollLeft = scrollLeft;
            document.documentElement.scrollLeft = scrollLeft;
        }
        if(currentTab!=null){
            if(document.getElementsByName(currentTab)[0] !=null) {
                //document.getElementsByName(currentTab)[0].click();

                openTab(document.getElementsByName(currentTab)[0].event,currentTab);
            }
            else{
                //document.getElementById("defaultOpen").click();
                openTab(document.getElementById("defaultOpen").event,document.getElementById("defaultOpen").name);
            }
        }
        else{
            //document.getElementById("defaultOpen").click();
            openTab(document.getElementById("defaultOpen").event,document.getElementById("defaultOpen").name);
        }

        Delete_Cookie("scrollTop");
        Delete_Cookie("scrollLeft");
    }

    function Refresh()
    {
        document.cookie = 'scrollTop=' + f_scrollTop();
        document.cookie = 'scrollLeft=' + f_scrollLeft();
        document.location.reload(true);
    }

    //Setting the cookie for vertical position
    function f_scrollTop() {
        return f_filterResults (
                window.pageYOffset ? window.pageYOffset : 0,
                document.documentElement ? document.documentElement.scrollTop : 0,
                document.body ? document.body.scrollTop : 0
        );
    }

    function f_filterResults(n_win, n_docel, n_body) {
        var n_result = n_win ? n_win : 0;
        if (n_docel && (!n_result || (n_result > n_docel)))
            n_result = n_docel;
        return n_body && (!n_result || (n_result > n_body)) ? n_body : n_result;
    }

    //Setting the cookie for horizontal position
    function f_scrollLeft() {
        return f_filterResults (
                window.pageXOffset ? window.pageXOffset : 0,
                document.documentElement ? document.documentElement.scrollLeft : 0,
                document.body ? document.body.scrollLeft : 0
        );
    }


    function Delete_Cookie(name)
    {
        document.cookie = name + "=" + ";expires=Thu, 01-Jan-1970 00:00:01 GMT";

    }

    function getScroll(){
        document.cookie = 'scrollTop=' + f_scrollTop();
        document.cookie = 'scrollLeft=' + f_scrollLeft();
        document.location.reload(true);
    }

    window.onload=doload;

</script>
