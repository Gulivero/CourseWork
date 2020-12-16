using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using HumanResourceServer.Models;
using HumanResourceServer.Utils;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace HumanResourceServer.Controllers
{
    [Route("api/[controller]")]
    public class LoginController : Controller
    {
        HumanResourceContext dbContext = new HumanResourceContext();


        // POST api/<controller>
        [HttpPost]
        public string Post([FromBody]Workers value)
        {
            //Check exists
            if (dbContext.Workers.Any(user => user.Login.Equals(value.Login)))
            {
                Workers user = dbContext.Workers.Where(u => u.Login.Equals(value.Login)).First();

                //Calculate hash password from data of Client and compare with hash in server with salt
                var client_post_hash_password = Convert.ToBase64String(
                    Common.SaltHashPassword(
                        Encoding.ASCII.GetBytes(value.Password),
                        Convert.FromBase64String(user.Salt)));

                if (client_post_hash_password.Equals(user.Password))
                {
                    return JsonConvert.SerializeObject(user);
                }
                else
                {
                    return JsonConvert.SerializeObject("Wrong Password");
                }

            }
            else
            {
                return JsonConvert.SerializeObject("User not existing in Database");
            }

        }

    }
}
