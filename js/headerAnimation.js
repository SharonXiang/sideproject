<!-- Animation JS -->
<script type="text/javascript">

    $('#bubble').removeClass("elements-visible");
    $('#bubble').addClass("bubblecover");

    $('#bathtubman').removeClass("elements-visible");
    $('#bathtubman').addClass("bathtubman");

    $('#propeller').removeClass("elements-visible");
    $('#propeller').addClass("propeller");


    setTimeout(function() {
    $('#whole').removeClass("elements-visible");
    $('#whole').addClass("appear");
    $('#bathtub').addClass("elements-visible");
    }, 1000)

    setTimeout(function() {
    $('#whole').removeClass("appear");
    $('#whole').addClass("floating");
    }, 1000)

    setTimeout(function() {
    $('#bubble').addClass("elements-visible");
    $('#bathtubman').addClass("elements-visible");
    $('#bathtubman').addClass("elements-visible");
    $('#propeller').addClass("elements-visible");
    }, 1000)   
</script>
