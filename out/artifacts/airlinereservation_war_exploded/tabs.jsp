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
        background-color: #a7bdcc;
    }

    /* Style the links inside the tab */
    div.tab a {
        display: block;
        text-align: center;
        padding: 14px 16px;
        text-decoration: none;
        transition: 0.3s;
        font-size: 17px;
        border-right:1px solid #a7bdcc;
        display: inline-block;
        font-weight:bold;
        color:white;
    }

    /* Change background color of links on hover */
    div.tab a:hover {
        background-color: #6983b5;
    }

    /* Create an active/current tablink class */
    div.tab a:focus, .active {
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
        }

        // Show the current tab, and add an "active" class to the link that opened the tab
        document.getElementById(tabName).style.display = "block";
        evt.currentTarget.className += " active";
    }
    document.getElementById("defaultOpen").click();
</script>
