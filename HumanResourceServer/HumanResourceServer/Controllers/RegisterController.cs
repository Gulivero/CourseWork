﻿using System;
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
    public class RegisterController : Controller
    {
        HumanResourceContext dbContext = new HumanResourceContext();



        // POST api/<controller>
        [HttpPost]
        public string Post([FromBody]Workers value)
        {
            //First we need check User have existing in database
            if (!dbContext.Workers.Any(user => user.Login.Equals(value.Login)))
            {
                Workers user = new Workers();
                user.Login = value.Login; // assign value from POST
                user.Name = value.Name;
                user.Surname = value.Surname;
                user.Sex = value.Sex;
                user.Department = value.Department;
                user.Position = value.Position;
                user.Salt = Convert.ToBase64String(Common.GetRandomSalt(16)); //Get Random Salt
                user.Password = Convert.ToBase64String(Common.SaltHashPassword(
                    Encoding.ASCII.GetBytes(value.Password),
                    Convert.FromBase64String(user.Salt)));

                //Add to Database
                try
                {
                    dbContext.Add(user);
                    dbContext.SaveChanges();
                    return JsonConvert.SerializeObject("Register successfully");
                }
                catch(Exception ex)
                {
                    return JsonConvert.SerializeObject(ex.Message);
                }
            }
            else
            {
                return JsonConvert.SerializeObject("User is existing in Database");
            }
        }


    }
}
