using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using HumanResourceServer.Models;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace HumanResourceServer.Controllers
{
    [Route("api/[controller]")]
    public class DeleteController : Controller
    {
        HumanResourceContext dbContext = new HumanResourceContext();


        // POST api/<controller>
        [HttpPost]
        public string Post([FromBody]Workers value)
        {
            if (dbContext.Workers.Any(user => user.Id.Equals(value.Id)))
            {
                Workers user = dbContext.Workers.Where(u => u.Id.Equals(value.Id)).First();
                try
                {
                    dbContext.Workers.Remove(user);
                    dbContext.SaveChanges();
                    return JsonConvert.SerializeObject("Delete successfully");
                }
                catch (Exception ex)
                {
                    return JsonConvert.SerializeObject(ex.Message);
                }
            }
            else
            {
                return JsonConvert.SerializeObject("User is not existing");
            }
            
        }
    }
}
