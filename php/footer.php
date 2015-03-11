
<!-- Footer For sharonxiang.ca -->
<!-- Includes custom Twitter feed and email form -->

      <div class="row">
          <div class="col-xs-12 col-sm-6 col-lg-8 "> 
          <div class = "footerwrapper">
           <p class = "footertext">Latest Tweets</p>
            <a class="twitter-timeline" width="600" height="200"
           href="https://twitter.com/SharonXiang" data-widget-id="364882737796087811"  data-tweet-limit = "3" data-chrome = "nofooter noborders noheader transparent" data-link-color="#999999" data-theme = "dark" ></a></div>  
           </div>  


<?if( !isset($_POST['submit'])):?> 
    

          <div class="col-xs-6 col-lg-4">        
              <div class = "emailbox">
                 <span id = "contact"></span>
                <form class="form-horizontal" method="POST"  action="#contact">
                <div class="form-group"><label class = "footertext">Name</label><input type="text" name = "username" class="form-control required name" data-placement="top" data-trigger="manual" data-content="Must be at least 3 characters long, and must only contain letters."></div>
                <div class="form-group"><label class = "footertext">E-Mail</label><input type="text" class="form-control required email" name="usermail" data-content="Must be a valid e-mail address (user@gmail.com)" data-trigger="manual" data-placement="top"></div>

                <div class="form-group"><label class = "footertext">Comments</label><textarea type="text" name = "usercomment" class="form-control comment" data-placement="top" data-trigger="manual" data-content="Please fill this out"></textarea> </div>

                <div class="form-group"> <button type="submit" name = "submit" class="btn-2 btn-2h btn pull-left">Contact Me!</button> <p class="help-block pull-left text-danger hide" id="form-error">&nbsp; The form is not valid. </p></div>
                </form>
              </div>
          </div><!-- span4 -->

        </div><!-- row -->


<?php else :
        $email_to = "123@gmail.com";

        $name=$_POST['username'];
        $mail=$_POST['usermail'];
        $comments=$_POST['usercomment'];


        function clean_string($string) {
          $bad = array("content-type","bcc:","to:","cc:","href");
          return str_replace($bad,"",$string);
        }

        $email_message .= "Name: ".clean_string($name)."\n";
        $email_message .= "Email: ".clean_string($mail)."\n";
        $email_message .= "Comments: ".clean_string($comments)."\n";

        $subject = "New Visitor Information";

        // create email headers
        $headers = 'From: '.$email_from."\r\n".
        'Reply-To: '.$email_from."\r\n" .
        'X-Mailer: PHP/' . phpversion();
        $email = mail($email_to, $subject, $email_message, $headers);  

        if($email){

            echo ' 
        <div class="col-xs-6 col-lg-4">        
              <div class = "emailbox">
              <span id = "contact"></span>
                <form class="form-horizontal" onsubmit="return goValidate()" action="" method="POST">
                <div class="form-group"><label class = "footertext">Name</label><input type="text" name = "username" class="form-control required name" data-placement="top" data-trigger="manual" data-content="Must be at least 3 characters long, and must only contain letters."></div>
                <div class="form-group"><label class = "footertext">E-Mail</label><input type="text" name = "usermail" class="form-control required email" data-placement="top" data-trigger="manual" data-content="Must be a valid e-mail address (user@gmail.com)"></div>
                <div class="form-group"><label class = "footertext">Comments</label><textarea type="text" name = "usercomment" class="form-control comment" data-placement="top" data-trigger="manual" data-content="Please fill this out"></textarea> </div>

                <div class="form-group"> <button type="submit" name = "submit" class="cus-button btn-sm move pull-left">Submit</button> <p class="help-block pull-left text-danger" id="form-error">&nbsp; SENT. </p></div>
                </form>
              </div>
          </div><!-- span4 -->

        </div><!-- row -->';
        }


?>
<? endif ?> 

